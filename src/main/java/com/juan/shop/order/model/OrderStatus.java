package com.juan.shop.order.model;

import lombok.Getter;

/**
 * @author guanhuan_li
 */
@Getter
public enum OrderStatus {
    /** 订单状态 */
    wuxiao("无效"),
    daifukuan("待付款"),
    yifukuan("已付款"),
    yiwancheng("已完成"),
    yijiesuan("已结算");

    private String zhName;

    OrderStatus(String zhName){
        this.zhName = zhName;
    }

}
