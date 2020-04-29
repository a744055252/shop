package com.juan.shop.token;

import com.juan.shop.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liguanhuan
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private IUserService userService;

    @Autowired
    private TokenManager tokenManager;

    private static final String TOKENHEADER = "X-Token";

    private static final String REQUESTHEADER = "Request-Type";

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {


        //先从url中取token
        String authToken = request.getHeader(TOKENHEADER);
        String requestType = request.getHeader(REQUESTHEADER);
        log.info("请求{},token校验：{}", requestType,authToken);

        if (StringUtils.isNotBlank(authToken)) {
            String username = null;
            try {
                username = tokenManager.getUserFromToken(authToken);
            } catch (Exception e) {
                log.error("token解析错误！", e);
                chain.doFilter(request, response);
            }


            log.info("checking authentication {}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //从已有的user缓存中取了出user信息
                UserDetails user = userService.loadUserByUsername(username);

                log.info("用户{}, token:{}", user.getUsername(), authToken);

                //检查token是否有效
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // todo 绑定微信校验
                // todo 邀请码校验


                //设置用户登录状态
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}