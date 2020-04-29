package com.juan.shop.user.tier;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.user.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 层级表
 * @author guanhuan_li
 */
@Data
@Entity
public class Tier extends AbstractEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 推广位id(京东) */
    private String positionId;

    /** 用户id {@link User#getId()} */
    private Long userId;

    /** 上级id {@link User#getId()} */
    private Long shangjiId;

    /** 导师id {@link User#getId()} */
    private Long daoshiId;

    
}
