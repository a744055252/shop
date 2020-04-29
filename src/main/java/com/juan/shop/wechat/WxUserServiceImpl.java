package com.juan.shop.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.StringUtils;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
@Service
@Slf4j
public class WxUserServiceImpl implements IWxUserSerivce {

    @Autowired
    private WxUserRepository wxUserRepository;

    @Override
    public void save(WxUser wxUser) {
        wxUserRepository.save(wxUser);
    }

    @Override
    public Optional<WxUser> findByOpenId(String openId) {
        return wxUserRepository.findByOpenId(openId);
    }

    @Override
    public boolean check(Long userId) {
        Optional<WxUser> wxUserOpt = wxUserRepository.findByUserId(userId);
        if (wxUserOpt.isPresent()) {
            WxUser wxUser = wxUserOpt.get();
            return StringUtils.isNotBlank(wxUser.getOpenId());
        }
        return false;
    }

    @Override
    public Optional<WxUser> findByUserId(Long userId) {
        return wxUserRepository.findByUserId(userId);
    }
}
