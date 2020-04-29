package com.juan.shop.spring;

import com.juan.shop.common.Result;
import com.juan.shop.token.ResultCode;
import com.juan.shop.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liguanhuan
 */
@Component("myAuthenctiationFailureHandler")
@Slf4j
public class MyAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        logger.info("登录失败");
        ResponseUtils.response(response, Result.valueOfFail(ResultCode.CODE_00016));
    }
}