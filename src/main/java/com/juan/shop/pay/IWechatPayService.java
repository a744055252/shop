package com.juan.shop.pay;

import com.juan.shop.pay.model.BankPayReq;
import com.juan.shop.pay.model.PayReq;

/**
 * @author guanhuan_li
 */
public interface IWechatPayService {

    String PAY_SUCCESS = "SUCCESS";

    String PAY_FAIL = "FAIL";

    /**
     * 付款至用户零钱
     * @param req
     * @return
     */
    WechatPayInfo pay(PayReq req);


    /**
     * 付款至用户银行卡
     *
     * */
    WechatBankPayInfo bankPay(BankPayReq req);


    /** 定时任务 */
    void task();
}
