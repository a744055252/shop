package com.juan.shop.user.model;

import com.juan.shop.menu.Menu;
import com.juan.shop.role.Role;
import lombok.Data;

import java.util.List;

/**
 * @author guanhuan_li
 */
@Data
public class UserInfoRes {

    private long id;

    private String username;

    private String password;

    private String name;

    private Gender gender;

    private Status status;

    private Type type;

    private List<Role> roles;

    private List<Menu> menus;
}
