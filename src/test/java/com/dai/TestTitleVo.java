package com.dai;

import com.dai.config.TitleAssistantConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTitleVo {

    @Autowired
    private TitleAssistantConfig.TitleAssistant titleAssistant;

    @Test
    public void test() {
        System.out.println(titleAssistant.chat("帮我写个游戏"));
    }

    @Test
    public void test1() {
        System.out.println(System.getenv("SEARCHAPI_API_KEY"));
    }
}
