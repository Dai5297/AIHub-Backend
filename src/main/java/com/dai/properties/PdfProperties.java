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
@ConfigurationProperties(prefix = "dai.pdf")
public class PdfProperties {

    /**
     * ES地址
     */
    private String host;

    /**
     * index名称
     */
    private String indexName;
}
