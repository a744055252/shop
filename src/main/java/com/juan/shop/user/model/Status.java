package com.juan.shop.user.model;

import lombok.Getter;

/**
 * @author liguanhuan
 */

@Getter
public enum Status {

    /** 启用 */
    qiyong("启用"),

    /** 停用 */
    tingyong("停用")
    ;

    private String zhName;

    Status(String zhName){
        this.zhName = zhName;
    }

}
