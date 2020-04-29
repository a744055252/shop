package com.juan.shop.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author guanhuan_li
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "login")
public class LoginConfig {

    private List<String> noCheckUrl;
}
