package com.juan.shop.user.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author guanhuan_li
 */
@Data
public class InviteReq {

    @NotBlank(message = "邀请码不能为空！")
    private String invite;
}
