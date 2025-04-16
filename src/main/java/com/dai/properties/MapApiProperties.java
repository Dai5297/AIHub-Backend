package com.dai.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Configuration
@ConfigurationProperties(prefix = "dai.api")
public class MapApiProperties {

    /**
     * 高德APIKEY
     */
    private String amapApi;

    /**
     * 百度APIKEY
     */
    private String baiduApi;
}
