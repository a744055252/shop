package com.juan.shop.user.invite;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.user.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 邀请码表
 * @author guanhuan_li
 */
@Data
@Entity
public class Invite extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 邀请码 */
    private String inviteCode;

    /** 用户id {@link User#getId()} */
    private Long userId;

    /** 上级id {@link User#getId()} */
    private Long shangjiId;

    /** 导师id {@link User#getId()} */
    private Long daoshiId;
}
