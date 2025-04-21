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

    private String host;

    private String dataBase;

    private Integer port;

    private String user;

    private String password;

    private String table;

    private Integer dimension;
}
