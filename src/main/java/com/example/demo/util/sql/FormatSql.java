package com.example.demo.util.sql;

import cn.hutool.core.io.FileUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    str = resolveUpdateSql(statement);
                } else if (statement instanceof Delete) {
                    // 解析删除binlog语句
                    str = resolveDeleteSql(statement);
                } else if (statement instanceof Insert) {
                    Insert insertStatement = (Insert) statement;
                    insertStatement.getItemsList();
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

    /**
     * 解析binlog的update语句，剔除set关键字包含的列和where包含的列值一样的列，where条件只保留id和companyId。
     *
     * 例子：
     *   入参：update table set id=1, name='张三', companyId='123', age=12 where id=1, name='张三', companyId='123', age=15;
     *   返回结果：update table set age=12 where id=1, companyId='123';
     *
     * @param statement
     * @return
     */
    private static String resolveUpdateSql(Statement statement) {
        Update updateStatement = (Update) statement;

        // 去除schema
        updateStatement.getTable().setSchemaName(null);

        // 获取更新内容
        Map<String, Object> updateStringMap = new HashMap<>();
        Map<String, UpdateSet> updateSetMap = new HashMap<>();
        for (UpdateSet updateSet : updateStatement.getUpdateSets()) {
            String setColum = updateSet.getColumns().get(0).toString();
            String setValue = updateSet.getExpressions().get(0).toString();
            updateStringMap.put(setColum, setValue);
            updateSetMap.put(setColum, updateSet);
        }

        // 获取更新条件
        List<Expression> expressionList = new ArrayList<>();
        if (updateStatement.getWhere() != null) {
            AndExpression condition = updateStatement.getWhere(AndExpression.class);
            getAllAndExpression(condition, expressionList);
        }
        Map<String, Object> whereMap = new HashMap<>();
        Map<String, Expression> whereExpressionMap = new HashMap<>();
        for (Expression expression : expressionList) {
            String whereColumName;
            String whereColumValue;
            if (expression instanceof EqualsTo) {
                whereColumName = getColumNameByEqualsToExpression(expression);
                whereColumValue = getColumValueByEqualsToExpression(expression);
            } else {
                whereColumName = ((IsNullExpression) expression).getLeftExpression().toString();
                whereColumValue = "NULL";
            }
            whereMap.put(whereColumName, whereColumValue);
            whereExpressionMap.put(whereColumName, expression);
        }

        // 删除binlog更新字段和where字段值相同的列。
        updateStringMap.entrySet().removeAll(whereMap.entrySet());

        // 组装结果
        Update updateResult = new Update();
        updateResult.setTable(updateStatement.getTable());
        updateStringMap.entrySet().stream().forEach(updateSetMapEntry -> {
            updateResult.addUpdateSet(updateSetMap.get(updateSetMapEntry.getKey()));
        });
        AndExpression andExpression = new AndExpression();
        if (whereExpressionMap.get("id") != null) {
            andExpression.setLeftExpression(whereExpressionMap.get("id"));
        }
        Expression companyIdExpression = null;
        if (whereExpressionMap.get("companyId") != null) {
            companyIdExpression = whereExpressionMap.get("companyId");
        }else if (whereExpressionMap.get("company_id") != null) {
            companyIdExpression = whereExpressionMap.get("company_id");
        }
        andExpression.setRightExpression(companyIdExpression);
        updateResult.setWhere(andExpression);
        return updateResult.toString() + ";";
    }

    /**
     * 解析删除binlog语句，返回delete from tableName where id = ? and companyId = ?;语句
     *
     * @param statement
     * @return
     */
    private static String resolveDeleteSql(Statement statement) {
        List<Expression> expressionList = new ArrayList<>();
        Delete deleteStatement = (Delete) statement;
        String tableName = deleteStatement.getTable().getName();
        if (deleteStatement.getWhere() != null) {
            AndExpression condition = deleteStatement.getWhere(AndExpression.class);
            getAllAndExpression(condition, expressionList);
        }
        // 删除语句,where条件只保留id,companyId两个条件
        StringBuffer whereBuffer = new StringBuffer();
        for (Expression expression : expressionList) {
            String whereColumName = "";
            String whereColumValue = "";
            if (expression instanceof EqualsTo) {
                whereColumName = getColumNameByEqualsToExpression(expression);
                whereColumValue = getColumValueByEqualsToExpression(expression);
            }
            if ("id".equals(whereColumName) || "companyId".equals(whereColumName) || "company_id".equals(whereColumName)) {
                whereBuffer.append(" AND ");
                whereBuffer.append(whereColumName);
                whereBuffer.append(" = ");
                whereBuffer.append(whereColumValue);
            }
        }
        // 去除第一个AND关键字
        String whereStr = whereBuffer.toString().replaceFirst(" AND ", "");
        return "DELETE FROM " + tableName + " WHERE " + whereStr + ";";
    }

    private static String getColumNameByEqualsToExpression(Expression expression) {
        return ((Column)((EqualsTo) expression).getLeftExpression()).getColumnName();
    }

    private static String getColumValueByEqualsToExpression(Expression expression) {
        return ((EqualsTo) expression).getRightExpression().toString();
    }

}
