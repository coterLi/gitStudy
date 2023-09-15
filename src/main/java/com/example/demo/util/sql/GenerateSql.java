package com.example.demo.util.sql;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author nelson.li
 * @date 2023/8/17
 **/
public class GenerateSql {
    private static final String PUSH = "PUSH";
    private static final String APP_MY_MESSAGE = "APP_MY_MESSAGE";
    public static void generate() throws IOException {
        BufferedReader br = FileUtil.getReader("C:\\Users\\3278\\Desktop\\数据修复.sql", Charset.forName("utf-8"));
        String str = br.readLine();
        while (str != null) {
            JSONObject message = JSONObject.parseObject(str);
            if (APP_MY_MESSAGE.equals(message.getJSONObject("content").getJSONObject("payload").getString("messageModeType"))) {
                System.out.println("-----");
            }
            str = br.readLine();
        }
    }
}
