package com.juan.shop.user.model;

import lombok.Data;

/**
 * @author guanhuan_li
 */
@Data
public class WxLoginRes {

    /** 登录token */
    private String token;

    /** 是否绑定邀请码 */
    private boolean flag;

}
