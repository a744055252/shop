package com.juan.shop.user.model;

import lombok.Getter;

/**
 * @author liguanhuan
 */

@Getter
public enum Gender {

    /** 男 */
    man("男"),
    /** 女 */
    woman("女");

    private String zhName;

    Gender(String zhName){
        this.zhName = zhName;
    }

    public static Gender valueOfWx(String gender) {
        Gender result;

        // 0：未知、1：男、2：女
        switch (gender) {
            case "1" : result = Gender.man;
                break;
            case "2" : result = Gender.woman;
                break;
            default:
                result = Gender.man;
        }
        return result;

    }
}
