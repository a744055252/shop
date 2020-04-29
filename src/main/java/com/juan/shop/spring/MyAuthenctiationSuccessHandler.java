package com.juan.shop.spring;

import com.juan.shop.common.Result;
import com.juan.shop.token.TokenManager;
import com.juan.shop.user.User;
import com.juan.shop.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liguanhuan
 */
@Component
@Slf4j
public class MyAuthenctiationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        String token = tokenManager.createToken(user.getUsername());

        log.info("{}登录成功, token:{}", user.getUsername(), token);
        ResponseUtils.response(response, Result.valueOfOk(token));
    }
}