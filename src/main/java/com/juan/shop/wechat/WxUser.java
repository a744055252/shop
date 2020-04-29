package com.juan.shop.wechat;

import com.juan.shop.user.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 关注用户的微信信息
 * @author guanhuan_li
 */
@Data
@Entity
public class WxUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户id {@link User#getId()} */
    private Long userId;

    private String openId;

    private String unionId;

    private String nickName;

    private String avatarUrl;

    /** 0：未知、1：男、2：女 */
    private String gender;

    private String city;

    private String province;

    private String country;

    private String language;

}
