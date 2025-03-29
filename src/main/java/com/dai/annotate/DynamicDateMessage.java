package com.dai.annotate;

import dev.langchain4j.service.SystemMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SystemMessage("""
    你的名字叫AI助手，你可以帮助用户解决各种问题，
    对应用户的问题如果你不知道答案则委婉的拒绝用户，禁止编制数据用于任何回答
    如果用户的问题与日期有关要结合今天的日期 #{T(java.time.LocalDate).now()}
    请以中文回答。
    """)
public @interface DynamicDateMessage {}
