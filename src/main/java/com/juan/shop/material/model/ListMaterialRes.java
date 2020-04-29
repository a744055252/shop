package com.juan.shop.material.model;

import lombok.Data;

/**
 * @author guanhuan_li
 */
@Data
public class ListMaterialRes {

    private Long id;

    /** 内容 */
    private String content;

    /** 图片url */
    private String img;

    /** 推广链接 */
    private String url;

    /** 创建人*/
    private String userName;

}
