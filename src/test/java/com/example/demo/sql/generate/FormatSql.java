package com.example.demo.sql.generate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlUtil;
import com.alibaba.fastjson.JSONObject;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.util.validation.validator.InsertValidator;
import org.apache.commons.lang3.StringUtils;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
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
        BufferedReader br = FileUtil.getReader("C:\\Users\\3278\\Desktop\\数据修复.sql", Charset.forName("utf-8"));
        FileUtil.del("C:\\Users\\3278\\Desktop\\result-1.sql");
        BufferedWriter bw = FileUtil.getWriter("C:\\Users\\3278\\Desktop\\result-1.sql", Charset.forName("utf-8"), true);
        String str = br.readLine();
        int i = 0;
        while (str != null) {
            if ("".equals(str)) {
                str = br.readLine();
                continue;
            }
            try {
                if (str.contains("UPDATE plan_ot_person_evaluate_norm_detail")) {
                    Statement statement = CCJSqlParserUtil.parse(str);
                    if (statement instanceof Update) {
                        Update updateStatement = (Update) statement;

                        // 表名
                        String tableName = updateStatement.getTable().getName();

                        // 获取 SET 子句条件
                        String setValue = "";
                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
                            if ("plan_ot_person_id".equals(updateSet.getColumns().get(0).getColumnName())) {
                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
                            }
                        }
                        // 获取 WHERE 子句条件
                        Expression whereCondition = updateStatement.getWhere();
                        String[] wheres = whereCondition.toString().split("AND");
                        String whereResult = "";
                        for (String where : wheres) {
                            if (where.contains("id = ")) {
                                whereResult = where;
                            }
                        }
                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
                    }

                } else if (str.contains("UPDATE plan_rt_template_ntype_norm_ectype ")) {
                    Statement statement = CCJSqlParserUtil.parse(str);
                    if (statement instanceof Update) {
                        Update updateStatement = (Update) statement;

                        // 表名
                        String tableName = updateStatement.getTable().getName();

                        // 获取 SET 子句条件
                        String setValue = "";
                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
                            if ("ntypeectype_for_normsectype".equals(updateSet.getColumns().get(0).getColumnName())) {
                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
                            }
                        }
                        // 获取 WHERE 子句条件
                        Expression whereCondition = updateStatement.getWhere();
                        String[] wheres = whereCondition.toString().split("AND");
                        String whereResult = "";
                        for (String where : wheres) {
                            if (where.contains("id = ")) {
                                whereResult = where;
                            }
                        }
                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
                    }
                } else if (str.contains("UPDATE plan_rt_template_ntype_ectype ")) {
                    Statement statement = CCJSqlParserUtil.parse(str);
                    if (statement instanceof Update) {
                        Update updateStatement = (Update) statement;

                        // 表名
                        String tableName = updateStatement.getTable().getName();

                        // 获取 SET 子句条件
                        String setValue = "";
                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
                            if ("templateectype_for_ntypesectype".equals(updateSet.getColumns().get(0).getColumnName())) {
                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
                            }
                        }
                        // 获取 WHERE 子句条件
                        Expression whereCondition = updateStatement.getWhere();
                        String[] wheres = whereCondition.toString().split("AND");
                        String whereResult = "";
                        for (String where : wheres) {
                            if (where.contains("id = ")) {
                                whereResult = where;
                            }
                        }
                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
                    }
                } else if (str.contains("UPDATE approval_process_log ")) {
                    Statement statement = CCJSqlParserUtil.parse(str);
                    if (statement instanceof Update) {
                        Update updateStatement = (Update) statement;

                        // 表名
                        String tableName = updateStatement.getTable().getName();

                        // 获取 SET 子句条件
                        String setValue = "";
                        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
                            if ("audit_request_id".equals(updateSet.getColumns().get(0).getColumnName())) {
                                setValue = " SET " + updateSet.getColumns().get(0).getColumnName() + " = " + updateSet.getExpressions().get(0);
                            }
                        }
                        // 获取 WHERE 子句条件
                        Expression whereCondition = updateStatement.getWhere();
                        String[] wheres = whereCondition.toString().split("AND");
                        String whereResult = "";
                        for (String where : wheres) {
                            if (where.contains("id = ")) {
                                whereResult = where;
                            }
                        }
                        str = "UPDATE " + tableName + setValue + " WHERE " + whereResult + ";";
                    }
                }  else if (str.contains("INSERT INTO plan_ot_person(")) {
//                    str = "";
                }  else if (str.contains("INSERT INTO asso_rt_staff(")) {
//                    str = "";
                }  else if (str.contains("INSERT INTO plan_rt_template_ntype_ectype(")) {
//                    str = "";
//                    System.out.println(str);
                }  else if (str.contains("INSERT INTO plan_rt_template_ntype_norm_ectype(")) {
//                    str = "";
                }  else if (str.contains("INSERT INTO assessment_audit_request(")) {
                    str = "";
//                    System.out.println(str);
                }  else if (str.contains("INSERT INTO assessment_approval_process(")) {
                    str = "";
//                    System.out.println(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            bw.write(str);
            bw.newLine();
            str = br.readLine();
        }
        bw.close();
    }
}
