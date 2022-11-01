package com.example.demo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private Environment environment;

    @Test
    public void propertiesTest() {
        Reader reader = FileUtil.getReader("xlsx/export_result—amazon.csv", StandardCharsets.UTF_8);
        CsvReader csvReader = CsvUtil.getReader();
        List<Map<String, String>> csvDataList = csvReader.readMapList(reader);

        for (int i = 0; i < 202211; i++) {
            String mysqlUrl = environment.getProperty("db" + i + ".datasource.url");
            if (StringUtils.isNotBlank(mysqlUrl)) {
                boolean checkResult = true;
                for (Map<String, String> csvDataMap : csvDataList) {
                    String dbStr = csvDataMap.get("\uFEFFhost") + ":" + csvDataMap.get("port") + "/" + csvDataMap.get("schemaName");
                    if (mysqlUrl.contains(dbStr)) {
                        checkResult = false;
                        break;
                    }
                }
                if (checkResult) {
                    System.out.println("数据库连接不存在" + mysqlUrl);
                }
            }
        }
    }
}
