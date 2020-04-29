package com.juan.shop.goods;

import com.juan.shop.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 商品
 * @author guanhuan_li
 */
@Data
@Entity
public class Goods extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 商品名 */
    private String name;

    /** 商品单价 */
    private Double price;

    /** 商品第三方id */
    private Long skuId;

    /** 一级类目id */
    private Long cid1;

    /** 二级类目id */
    private Long cid2;

    /** 三级类目id */
    private Long cid3;

}
