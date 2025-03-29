package com.dai;

import com.dai.config.CommonAssistantConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTitleVo {

    @Autowired
    private CommonAssistantConfig.CommonAssistant commonAssistant;

    @Test
    public void test() {
        System.out.println(commonAssistant.chat("帮我写个游戏"));
    }

    @Test
    public void test1() {
//        System.out.println(System.getenv("TAVILY_API_KEY"));

    }
}
