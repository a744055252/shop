package com.juan.shop.spring;

import com.juan.shop.common.LoginConfig;
import com.juan.shop.role.IRoleService;
import com.juan.shop.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 *
 * 暂不使用
 *
 * 接收用户请求的地址，返回访问该地址需要的所有权限
 *
 * @author liguanhuan
 */
@Component
@Slf4j
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
 
    @Autowired
    private IRoleService roleService;

    @Autowired
    private LoginConfig loginConfig;

    /**
     * 接收用户请求的地址，返回访问该地址需要的所有权限
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //得到用户的请求地址,控制台输出一下
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        String paramFlag = "?";
        if (requestUrl.contains(paramFlag)) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf(paramFlag));
        }
        log.info("用户请求的地址是：" + requestUrl);

        if (loginConfig.getNoCheckUrl().contains(requestUrl)) {
            return null;
        }

        List<Role> roles = roleService.findByUrl(requestUrl);
        // 没有匹配的角色则说明大家都可以访问
        if (roles.isEmpty()) {
            return SecurityConfig.createList(IRoleService.OTHER_ROLE);
        }

        String[] result = roles.stream().map(Role::getAuth).toArray(String[]::new);
        return SecurityConfig.createList(result);
    }
 
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
 
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    public static void main(String[] args) {
        String s = "/user/invite/get?X-Token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ3ZWNoYXRfb0ctMW41SVlNQy1teFp";
        String substring = s.substring(0, s.indexOf("?"));
        System.out.println(substring);
    }
}