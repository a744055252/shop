package com.juan.shop.pay.model;


/**
 * @author guanhuan_li
 */
public enum PayStatus {

    /**
     * 订单的支付状态
     *
     */
    ERROR("订单未成功提交"),

    SUCCESS("转账成功"),

    FAILED("转账失败"),

    PROCESSING("处理中"),

    ;

    private String zhName;

    PayStatus(String zhName){
        this.zhName = zhName;
    }

    public String getZhName() {
        return this.zhName;
    }
}
