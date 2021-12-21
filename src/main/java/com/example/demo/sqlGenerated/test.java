package com.example.demo.sqlGenerated;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailUtil;

import java.util.ArrayList;

/**
 * @author nelson.li
 * @date 2021/12/15
 **/
public class test {
    public static void main(String[] args) {
        ArrayList<String> tos = CollUtil.newArrayList(
                "person1@bbb.com",
                "person2@bbb.com",
                "person3@bbb.com",
                "person4@bbb.com");

        MailUtil.send(tos, "测试", "邮件来自Hutool群发测试", false);
    }
}
