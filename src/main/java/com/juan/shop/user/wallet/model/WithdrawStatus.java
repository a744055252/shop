package com.juan.shop.user.wallet.model;

import lombok.Getter;

/**
 * 提现状态
 * @author guanhuan_li
 */
@Getter
public enum WithdrawStatus {

    /** 提现状态 */
    caogao("草稿"),

    weitixian("未提现"),

    chucuo("提现出错"),

    tixianzhong("提现中"),

    wancheng("完成"),

    ;


    private String zhName;

    WithdrawStatus(String zhName){
        this.zhName = zhName;
    }

}
