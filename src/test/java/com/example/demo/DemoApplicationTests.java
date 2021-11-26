package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        // cid:a63b33f6-ead7-46ce-bc77-e3b5fbcbc5f9&fileName=th.jpg
        // cid:a63b33f6-ead7-46ce-bc77-e3b5fbcbc5f9
        String str = "<img src=\"cid:a63b33f6-ead7-46ce-bc77-e3b5fbcbc5f9&fileName=th - 啊啊.jpg\" title=\"th.jpg\" alt=\"th.jpg\" />";
        str = str.replaceAll("(cid:)([a-zA-Z0-9-]*)([^&\\S*\\s*])(\"$)", "$1$2");
        System.out.println(str);
    }
}
