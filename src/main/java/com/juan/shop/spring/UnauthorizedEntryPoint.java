package com.juan.shop.spring;

import com.juan.shop.common.Result;
import com.juan.shop.token.ResultCode;
import com.juan.shop.utils.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liguanhuan
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        Result<Void> res = Result.valueOfFail(ResultCode.CODE_00005);
        ResponseUtils.response(response, res);
    }



}