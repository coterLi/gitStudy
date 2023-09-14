package com.example.demo.sql.generate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlUtil;
import com.alibaba.fastjson.JSONObject;
import net.sf.jsqlparser.expression.ArrayExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SetStatementDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;
import net.sf.jsqlparser.util.validation.validator.InsertValidator;
import net.sf.jsqlparser.util.validation.validator.UpdateValidator;
import org.apache.commons.lang3.StringUtils;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.stream.Collectors;

/**
 * @author nelson.li
 * @date 2023/9/6
 **/
public class FormatSql {
    /**
     * 格式化sql，去除binlog逆向sql结尾的字符串“ #start 293826963 end 299011040 time 2023-09-05 13:40:31”
     *
     * @throws IOException
     */
    public static void format() throws IOException {
        BufferedReader br = FileUtil.getReader("C:\\Users\\3278\\Desktop\\数据修复.sql", Charset.forName("utf-8"));
        FileUtil.del("C:\\Users\\3278\\Desktop\\result-1.sql");
        BufferedWriter bw = FileUtil.getWriter("C:\\Users\\3278\\Desktop\\result-1.sql", Charset.forName("utf-8"), true);
        String str = br.readLine();
        int i = 0;
        while (str != null) {
            int index = str.indexOf(" #start");
            String result = StringUtils.substring(str, 0, index);
            bw.write(result);
            bw.newLine();
            i++;
            str = br.readLine();
            System.out.println("写到 " + i);
        }
        bw.close();
    }


    /**
     * 格式化sql，去除binlog逆向sql结尾的字符串“ #start 293826963 end 299011040 time 2023-09-05 13:40:31”
     *
     * @throws IOException
     */
    public static void formatV2() throws IOException {
        BufferedReader br = FileUtil.getReader("C:\\Users\\3278\\Desktop\\数据修复 - 副本.sql", Charset.forName("utf-8"));
        FileUtil.del("C:\\Users\\3278\\Desktop\\result-1.sql");
        BufferedWriter bw = FileUtil.getWriter("C:\\Users\\3278\\Desktop\\result-1.sql", Charset.forName("utf-8"), true);
        String str = br.readLine();
        while (str != null) {
            if ("".equals(str)) {
                str = br.readLine();
                continue;
            }

            // 格式化sql，去掉binlog文件中sql末尾的“#start 123295723 end 123300012 time 2023-09-11 10:48:16”字符串
            str = replaceBinlogSpecialStr(str);

            // 去除binlog脚本中sql的“`”字符
            str = str.replaceAll("`", "");

            try {
                Statement statement = CCJSqlParserUtil.parse(str);

                if (statement instanceof Select) {
                    Select selectStatement = (Select) statement;
                } else if (statement instanceof Update) {
                    Update updateStatement = (Update) statement;

                    // 获取表名
                    String schemaName = updateStatement.getTable().getSchemaName();
                    String tableName = updateStatement.getTable().getName();

                    // 获取更新内容
                    Map<String, Object> updateSetMap = new HashMap<>();
                    for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
                        String setColum = updateSet.getColumns().get(0).toString();
                        String setValue = updateSet.getExpressions().get(0).toString();
                        updateSetMap.put(setColum, setValue);
                    }

                    // 获取更新条件
                    List<Expression> expressionList = new ArrayList<>();
                    Map<String, Object> whereMap = new HashMap<>();
                    if (updateStatement.getWhere() != null) {
                        AndExpression condition = updateStatement.getWhere(AndExpression.class);
                        getAllAndExpression(condition, expressionList);
                    }
                    for (Expression expression : expressionList) {
                        String whereColumName;
                        String whereColumValue;
                        if (expression instanceof EqualsTo) {
                            EqualsTo equalsTo = ((EqualsTo) expression).getLeftExpression(EqualsTo.class);
                            whereColumName = equalsTo.getLeftExpression().toString();
                            whereColumValue = equalsTo.getRightExpression().toString();
                        } else {
                            whereColumName = ((IsNullExpression) expression).getLeftExpression().toString();
                            whereColumValue = "NULL";
                        }
                        whereMap.put(whereColumName, whereColumValue);
                    }
                    System.out.println(123);
                } else if (statement instanceof Delete) {
                    Delete deleteStatement = (Delete) statement;
                    System.out.println("删除的表" + deleteStatement.getTable());
                    System.out.println("删除条件的列" + deleteStatement.getWhere());
                }

                if (str.contains("UPDATE plan_ot_person_evaluate_norm_detail")) {
//                    Statement statement = CCJSqlParserUtil.parse(str);
//                    if (statement instanceof Update) {
//                        Update updateStatement = (Update) statement;
//
//                        // 表名
//                        String tableName = updateStatement.getTable().getName();
//
//                        // 获取 SET 子句条件
//                        String setValue = "";
//                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
//                            if ("plan_ot_person_id".equals(updateSet.getColumns().get(0).getColumnName())) {
//                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
//                            }
//                        }
//                        // 获取 WHERE 子句条件
//                        Expression whereCondition = updateStatement.getWhere();
//                        String[] wheres = whereCondition.toString().split("AND");
//                        String whereResult = "";
//                        for (String where : wheres) {
//                            if (where.contains("id = ")) {
//                                whereResult = where;
//                            }
//                        }
//                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
//                    }

                } else if (str.contains("UPDATE plan_rt_template_ntype_norm_ectype ")) {
//                    Statement statement = CCJSqlParserUtil.parse(str);
//                    if (statement instanceof Update) {
//                        Update updateStatement = (Update) statement;
//
//                        // 表名
//                        String tableName = updateStatement.getTable().getName();
//
//                        // 获取 SET 子句条件
//                        String setValue = "";
//                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
//                            if ("ntypeectype_for_normsectype".equals(updateSet.getColumns().get(0).getColumnName())) {
//                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
//                            }
//                        }
//                        // 获取 WHERE 子句条件
//                        Expression whereCondition = updateStatement.getWhere();
//                        String[] wheres = whereCondition.toString().split("AND");
//                        String whereResult = "";
//                        for (String where : wheres) {
//                            if (where.contains("id = ")) {
//                                whereResult = where;
//                            }
//                        }
//                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
//                    }
                } else if (str.contains("UPDATE plan_rt_template_ntype_ectype ")) {
//                    Statement statement = CCJSqlParserUtil.parse(str);
//                    if (statement instanceof Update) {
//                        Update updateStatement = (Update) statement;
//
//                        // 表名
//                        String tableName = updateStatement.getTable().getName();
//
//                        // 获取 SET 子句条件
//                        String setValue = "";
//                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
//                            if ("templateectype_for_ntypesectype".equals(updateSet.getColumns().get(0).getColumnName())) {
//                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
//                            }
//                        }
//                        // 获取 WHERE 子句条件
//                        Expression whereCondition = updateStatement.getWhere();
//                        String[] wheres = whereCondition.toString().split("AND");
//                        String whereResult = "";
//                        for (String where : wheres) {
//                            if (where.contains("id = ")) {
//                                whereResult = where;
//                            }
//                        }
//                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
//                    }
                } else if (str.contains("UPDATE approval_process_log ")) {
//                    Statement statement = CCJSqlParserUtil.parse(str);
//                    if (statement instanceof Update) {
//                        Update updateStatement = (Update) statement;
//
//                        // 表名
//                        String tableName = updateStatement.getTable().getName();
//
//                        // 获取 SET 子句条件
//                        String setValue = "";
//                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
//                            if ("audit_request_id".equals(updateSet.getColumns().get(0).getColumnName())) {
//                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
//                            }
//                        }
//                        // 获取 WHERE 子句条件
//                        Expression whereCondition = updateStatement.getWhere();
//                        String[] wheres = whereCondition.toString().split("AND");
//                        String whereResult = "";
//                        for (String where : wheres) {
//                            if (where.contains("id = ")) {
//                                whereResult = where;
//                            }
//                        }
//                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
//                    }
                } else if (str.contains("INSERT INTO plan_ot_person(")) {
//                    str = "";
                } else if (str.contains("INSERT INTO asso_rt_staff(")) {
//                    str = "";
                } else if (str.contains("INSERT INTO plan_rt_template_ntype_ectype(")) {
//                    str = "";
//                    System.out.println(str);
                } else if (str.contains("INSERT INTO plan_rt_template_ntype_norm_ectype(")) {
//                    str = "";
                } else if (str.contains("INSERT INTO assessment_audit_request(")) {
                    str = "";
//                    System.out.println(str);
                } else if (str.contains("INSERT INTO assessment_approval_process(")) {
                    str = "";
//                    System.out.println(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            bw.write(str);
//            bw.newLine();
            str = br.readLine();
        }
        bw.close();
    }

    /**
     * 格式化sql，去掉binlog文件中sql末尾的“#start 123295723 end 123300012 time 2023-09-11 10:48:16”字符串
     *
     * @param binlogSql
     * @return
     */
    public static String replaceBinlogSpecialStr(String binlogSql) {
        int index = binlogSql.indexOf(" #start");
        return StringUtils.substring(binlogSql, 0, index);
    }

    public static List<Expression> getAllAndExpression(Expression expression, List<Expression> expressionList) {

        if (expression instanceof AndExpression) {
            AndExpression andExpression = (AndExpression) expression;
            expressionList.add(andExpression.getRightExpression());
            Expression leftExpression = andExpression.getLeftExpression();
            if (leftExpression instanceof AndExpression) {
                getAllAndExpression((AndExpression) leftExpression, expressionList);
            } else {
                expressionList.add(leftExpression);
            }
            return expressionList;
        }
        return expressionList;
    }

}
