package com.juan.shop.wechat;

import com.juan.shop.spring.WechatAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.Jscode2sessionResult;

/**
 * @author guanhuan_li
 */
@Slf4j
@Service
public class WechatServiceImpl implements IWechatService {

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Override
    public Jscode2sessionResult miniprogramLogin(String code) {
        return SnsAPI.jscode2session(wechatAuthProperties.getAppId(), wechatAuthProperties.getSecret(), code);
    }


}
