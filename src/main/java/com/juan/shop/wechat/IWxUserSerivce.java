package com.juan.shop.wechat;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface IWxUserSerivce {

    void save(WxUser wxUser);

    Optional<WxUser> findByOpenId(String openId);

    /**
     * 检查用户是否绑定微信
     * @param userId
     * @return
     */
    boolean check(Long userId);

    Optional<WxUser> findByUserId(Long userId);
}
