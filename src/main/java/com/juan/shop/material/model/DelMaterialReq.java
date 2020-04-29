package com.juan.shop.material.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author guanhuan_li
 */
@Data
public class DelMaterialReq {

    @NotNull(message = "素材id不能为空")
    private Long id;
}
