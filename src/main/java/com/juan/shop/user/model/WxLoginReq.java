package com.juan.shop.user.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author guanhuan_li
 */
@Data
public class WxLoginReq {

    @NotBlank(message = "code字段不能为空！")
    private String code;

    @NotBlank(message = "encryptedData字段不能为空！")
    private String encryptedData;

    @NotBlank(message = "iv字段不能为空！")
    private String iv;

}
