package com.dai.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *jwt token配置
 * @author dai
 */

@Setter
@Getter
@NoArgsConstructor
@ToString
@Configuration
@ConfigurationProperties(prefix = "dai.framework.jwt")
public class JwtTokenManagerProperties {

    /**
     *  签名密码
     */
    private String base64EncodedSecretKey;

    /**
     *  有效时间
     */
    private Integer ttl;
}
