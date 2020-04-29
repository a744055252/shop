package com.juan.shop.token;

import com.juan.shop.common.Result;
import com.juan.shop.user.User;
import com.juan.shop.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import utils.JsonUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author liguanhuan
 */
@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginUserReq userReq = JsonUtils.decodeJson(IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8), LoginUserReq.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userReq.getUsername(), userReq.getPassword(), new ArrayList<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) {
        User user = (User) auth.getPrincipal();
        String token = tokenManager.createToken(user.getUsername());
        log.info("用户[{}], 生成token[{}]", user.getUsername(), token);
        ResponseUtils.response(res, Result.valueOfOk(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException e) {
        response.setStatus(HttpStatus.OK.value());
        Result<Void> rm;
        if (e instanceof BadCredentialsException) {
            rm = Result.valueOfFail(ResultCode.CODE_00001);
        } else if (e instanceof UsernameNotFoundException) {
            rm = Result.valueOfFail(ResultCode.CODE_00011);
        } else if (e instanceof AuthenticationCredentialsNotFoundException) {
            rm = Result.valueOfFail(ResultCode.CODE_00003);
        } else if (e instanceof ProviderNotFoundException) {
            rm = Result.valueOfFail(ResultCode.CODE_10000);
        } else {
            rm = Result.valueOfFail(ResultCode.CODE_00013);
        }
        ResponseUtils.response(response, rm);
    }
}
