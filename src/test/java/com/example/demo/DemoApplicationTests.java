package com.example.demo;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.i18nResolve.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.UnicodeEscaper;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    public void propertiesTest() throws JsonProcessingException {
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

        Student student = new Student();
        String studentString = JSONObject.toJSONString(student);

        Student studenta = JSONObject.parseObject(studentString, Student.class);
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

}
