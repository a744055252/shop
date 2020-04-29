package com.juan.shop.three.jd;

import lombok.Getter;

/**
 * @author guanhuan_li
 */
@Getter
public enum Platform {

    /** 第三方平台 */
    jd("京东"),

    ;

    private String zhName;

    Platform(String zhName){
        this.zhName = zhName;
    }

}
