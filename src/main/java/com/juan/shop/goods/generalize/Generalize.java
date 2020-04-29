package com.juan.shop.goods.generalize;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.goods.Goods;
import com.juan.shop.material.Material;
import com.juan.shop.three.jd.Platform;
import com.juan.shop.user.User;
import com.juan.shop.user.tier.Tier;
import lombok.Data;

import javax.persistence.*;

/**
 * 商品的推广链接
 *
 * 推广链接的生成方式有2种：
 * 1. 系统后台生成，维度有商品、用户、平台
 * 2. 手动获取推广链接，这个场景用于手动新增录入素材时，录入人手动获取推广链接。
 *
 * @author guanhuan_li
 */
@Data
@Entity
public class Generalize extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 商品id {@link Goods#getId()} */
    private Long goodsId;
    
    /** 用户id {@link User#getId()} */
    private Long userId;
    
    /** 推广位id {@link Tier#getPositionId()} */
    private String positionId;

    /** 素材id {@link Material#getId()} */
    private Long materialId;

    /** 平台 */
    @Enumerated(EnumType.STRING)
    private Platform platform;

    /** 推广链接 */
    private String url;
}
