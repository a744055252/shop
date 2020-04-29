package com.juan.shop.goods.category;

import com.juan.shop.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 分类表
 * @author guanhuan_li
 */
@Data
@Entity
public class Category extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 父分类id */
    private Long parentId;

    /** 分类名 */
    private String name;

}
