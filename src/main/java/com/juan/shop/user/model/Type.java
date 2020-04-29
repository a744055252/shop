package com.juan.shop.user.model;

import lombok.Getter;

/**
 * @author liguanhuan
 */

@Getter
public enum Type {

    /** 普通用户 */
    normal("普通用户"),

    /** 管理员 */
    admin("管理员")

    ;

    private String zhName;

    Type(String zhName){
        this.zhName = zhName;
    }

}
