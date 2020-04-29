package com.juan.shop.user;

import com.juan.shop.user.model.UserInfoRes;
import com.juan.shop.user.model.WxLoginReq;
import com.juan.shop.user.model.WxLoginRes;
import com.juan.shop.user.tier.Tier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface IUserService {

    String createInvite(Long operId);

    WxLoginRes wechatLogin(WxLoginReq req);

    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;

    void add(User user);

    Optional<User> get(Long userId);

    UserInfoRes info(long id);

    void invite(Long operId, String inviteCode);

    Optional<Tier> getTier(Long userId);

    /**
     * 获取用户的推广位id
     * @param userId
     * @return
     */
    String getPositionId(Long userId);

    User findByPositionId(String positionId);
}
