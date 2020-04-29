package com.juan.shop.material.model;

import com.juan.shop.file.UploadFile;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author guanhuan_li
 */
@Data
public class EditMaterialReq {

    @NotNull(message = "素材id不能为空")
    private Long id;

    /** 推广商品url */
    @NotBlank(message = "推广商品url不能为空")
    private String goodsUrl;

    /** 内容 */
    private String content;

    /** 图片id {@link UploadFile#getId()} */
    private Long imgFileId;

    /** 用户id和对应的推广链接 */
    private Map<Long, String> id2url;

}
