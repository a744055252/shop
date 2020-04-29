package com.juan.shop.material;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.file.UploadFile;
import com.juan.shop.user.User;
import lombok.Data;

import javax.persistence.*;

/**
 * 素材
 * @author guanhuan_li
 */
@Data
@Entity
public class Material extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 创建人id {@link User#getId()}*/
    private Long userId;

    /** 图片id {@link UploadFile#getId()} */
    private Long imgFileId;

    /** 内容 */
    @Lob
    private String content;

    /** 推广商品url */
    private String goodsUrl;

}
