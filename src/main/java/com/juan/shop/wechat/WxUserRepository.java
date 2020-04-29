package com.juan.shop.wechat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface WxUserRepository extends JpaRepository<WxUser, Long> {

    Optional<WxUser> findByOpenId(String openId);

    Optional<WxUser> findByUserId(Long userId);

}
