package com.juan.shop.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liguanhuan
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAuthProperties {

    private String appId;

    private String secret;

    private String mchAppid;
    private String mchid;
    private String key;
    private String ip;

}