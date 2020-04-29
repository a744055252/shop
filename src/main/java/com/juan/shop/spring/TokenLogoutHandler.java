package com.juan.shop.spring;

import com.juan.shop.common.Result;
import com.juan.shop.token.TokenManager;
import com.juan.shop.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liguanhuan
 */
@Slf4j
public class TokenLogoutHandler implements LogoutHandler {
    private TokenManager tokenManager;

    public TokenLogoutHandler(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (token != null) {
            tokenManager.removeToken(token);
        }

        ResponseUtils.response(response, Result.valueOfOkNull());
    }
}