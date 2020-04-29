package com.juan.shop.utils;

import com.juan.shop.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author guanhuan_li
 */
public abstract class SpringUtils {

    public static User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
