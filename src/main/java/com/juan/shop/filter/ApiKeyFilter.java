package com.juan.shop.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 校验请求的apikey
 * @author liguanhuan
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "apiKeyFilter")
@Slf4j
public class ApiKeyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        StringBuilder sb = new StringBuilder();
        sb.append("\r\n-------------start-----------\r\n");
        sb.append("收到请求url：").append(((HttpServletRequest) servletRequest).getRequestURI()).append(" 请求参数：\r\n");
        String blank = "   ";
        //todo post获取不到数据
        for (Map.Entry<String, String[]> entry : servletRequest.getParameterMap().entrySet()) {
            for (String str : entry.getValue()) {
                sb.append(blank).append(entry.getKey()).append(" : ").append(str).append("\r\n");
            }
        }
        sb.append("-------------end-------------\r\n");
        log.info(sb.toString());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
