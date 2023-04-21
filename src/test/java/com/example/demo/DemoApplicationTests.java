package com.example.demo;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.i18nResolve.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.translate.UnicodeEscaper;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class DemoApplicationTests {

    @Test
    public void propertiesTest() throws JsonProcessingException {
//        ExcelUtil.getBigWriter().write(null);
        String applicantName = "enders";
        String respondentName = "nelson";
        String settingName = "#user_custom_oa_e19ba5ed4a7943d8bb30a15e173f3fff#";

        String title = i18nJoin("ihr360.workflow.0251", escapes("end!@#$%^&*()_+-=~`,.<>?/\\|ers"), escapes("nelson"), "#user_custom_oa_e19ba5ed4a7943d8bb30a15e173f3fff#");

        System.out.println("-----");
        System.out.println(title);
        System.out.println("-----");
        System.out.println(unescapes(title));
        System.out.println(unescapes("\\u003e"));


        UnicodeEscaper unicodeEscaper = new UnicodeEscaper();
        System.out.println(unicodeEscaper.translate("#$,\\&-"));

//        Student student = new Student();
//        String studentString = JSONObject.toJSONString(student);
//
//        Student studenta = JSONObject.parseObject(studentString, Student.class);
//
//        System.out.println("-----------------------");
//        System.out.println(0%2);
    }

    private static String escapes(String input) throws JsonProcessingException {
        return StringUtils.replaceEach(input, new String[]{"\\", ",", "#", "$", "&"}, new String[]{"\\u005C", "\\u002C", "\\u0023", "\\u0024", "\\u0026"});
    }

    private static String unescapes(String input) {
        UnicodeUnescaper unicodeUnescaper = new UnicodeUnescaper();
        return unicodeUnescaper.translate(input);
    }

    private static String i18nJoin(String i18nKey, String ...params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("$");
        stringBuilder.append(i18nKey);
        stringBuilder.append("&");
        stringBuilder.append(StringUtils.join(params, ","));
        stringBuilder.append("$");
        return stringBuilder.toString();
    }

    @Test
    public void orderTest() {
        List<Student> students = new ArrayList<>();
        Student student1 = new Student();
        student1.setDepartmentId(1L);
        student1.setDepartmentName("部门1");
        student1.setPositionName("开发");
        student1.setProbationEndDate("2022-01-02");
        students.add(student1);
        Student student2 = new Student();
        student2.setDepartmentId(2L);
        student2.setDepartmentName("部门2");
        student2.setPositionName("开发");
        student2.setProbationEndDate("2022-01-03");
        students.add(student2);
        Student student3 = new Student();
        student3.setDepartmentId(3L);
        student3.setDepartmentName("部门3");
        student3.setPositionName("开发");
        student3.setProbationEndDate("2022-01-03");
        students.add(student3);
        Student student4 = new Student();
        student4.setDepartmentId(3L);
        student4.setDepartmentName("部门3");
        student4.setPositionName("测试");
        student4.setProbationEndDate("2022-01-03");
        students.add(student4);
        Student student6 = new Student();
        student6.setDepartmentId(3L);
        student6.setDepartmentName("部门3");
        student6.setPositionName("UI");
        student6.setProbationEndDate("2022-01-03");
        students.add(student6);
        Student student5 = new Student();
        student5.setDepartmentId(3L);
        student5.setDepartmentName("部门3");
        student5.setPositionName("测试");
        student5.setProbationEndDate(null);
        students.add(student5);



        List<String> students2 = new ArrayList<>();

        students2.add("123");
        students2.add("456");

        System.out.println(StringUtils.join(new String[]{"1", "3"}));

        students = students.stream()
                .sorted(Comparator.comparing(Student::getProbationEndDate, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                if (o1 == null) {
                                    return o2 == null ? 0 : -1;
                                } else if (o2 == null) {
                                    return 1;
                                } else {
                                    try {
                                        Date o1ProbationEndDate = DateUtils.parseDate(o1, "yyyy-MM-dd");
                                        Date o2ProbationEndDate = DateUtils.parseDate(o2, "yyyy-MM-dd");
                                        return o1ProbationEndDate.compareTo(o2ProbationEndDate);
                                    } catch (ParseException e) {
                                        return 0;
                                    }
                                }
                            }
                        })
                        .thenComparing(Student::getDepartmentName, Comparator.nullsFirst(String::compareTo))
                        .thenComparing(Student::getPositionName, Comparator.nullsFirst(String::compareTo)))
                .collect(Collectors.toList());


        for (Student item : students) {
            System.out.println(JSONObject.toJSONString(item));
        }
    }

    @Test
    public void stringTest() {
        Date date1 = DateUtil.parse("2021-01-01 12:00:00", "yyyy-MM-dd HH:mm:ss");
        Date date2 = DateUtil.parse("2021-01-03 12:00:00", "yyyy-MM-dd HH:mm:ss");

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        boolean result = cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY);
    }

    @Test
    public void strTest() throws IOException {
        Set<String> companyIdSet = new HashSet<>();
        BufferedReader br = FileUtil.getReader("C:\\Users\\3278\\Downloads\\msg (13).txt", Charset.forName("utf-8"));
        resolve(br, companyIdSet);
        System.out.println(JSON.toJSONString(companyIdSet));
        System.out.println("--------------------------------------------------------------");
        companyIdSet.clear();
        BufferedReader br2 = FileUtil.getReader("C:\\Users\\3278\\Downloads\\msg (14).txt", Charset.forName("utf-8"));
        resolve(br2, companyIdSet);

        System.out.println(JSON.toJSONString(companyIdSet));
    }

    private void resolve(BufferedReader br, Set<String> companyIdSet) throws IOException {
        String str = br.readLine();
        while (str != null) {
            str = StringUtils.substring(str, str.indexOf("{"), str.lastIndexOf("}") + 1);
            JSONObject jsonObject = JSONObject.parseObject(str);
            JSONObject content = jsonObject.getJSONObject("content");
            JSONObject payload = content.getJSONObject("payload");
            String title = payload.getString("title");
            if ((title.contains("\\u005C")
                    || title.contains("\\u002C")
                    || title.contains("\\u005C")
                    || title.contains("\\u0023")
                    || title.contains("\\u0024")
                    || title.contains("\\u0026")
                    || title.contains("\\u002D")) && !title.contains("SN#")) {
                String companyId = payload.getString("companyId");
                String currentOperatorId = payload.getString("currentOperatorId");
                String processId = payload.getString("processId");

//                System.out.println(companyId + "、" + currentOperatorId + "、" + processId + "、");
//                System.out.println("--------------------------------------------------------------");

                companyIdSet.add(companyId);

            }
            str = br.readLine();
        }
    }
}
