package com.juan.shop.three.jd;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liguanhuan
 */
@Data
@Component
@ConfigurationProperties(prefix = "jd")
public class JdProperties {

    private String serverUrl;

    private String accessToken;

    private String appKey;

    private String appSecret;

    private String sideId;

}