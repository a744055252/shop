package com.juan.shop.material.model;

import com.juan.shop.file.UploadFile;
import lombok.Data;

import java.util.Map;

/**
 * @author guanhuan_li
 */
@Data
public class AddMaterialReq {

    /** 推广商品url */
    private String goodsUrl;

    /** 内容 */
    private String content;

    /** 图片id {@link UploadFile#getId()} */
    private Long imgFileId;

    /** 用户id和对应的推广链接 */
    private Map<Long, String> id2url;
}
