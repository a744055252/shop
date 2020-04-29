package com.juan.shop.wechat;

import weixin.popular.bean.sns.Jscode2sessionResult;

/**
 * @author guanhuan_li
 */
public interface IWechatService {

    Jscode2sessionResult miniprogramLogin(String code);

}
