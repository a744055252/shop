package com.juan.shop.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author guanhuan_li
 */
@Data
@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    private String signKey;

    private int expired;

}
