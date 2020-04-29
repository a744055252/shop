package com.juan.shop.pay.model;


/**
 * @author guanhuan_li
 */
public enum PayType {

    /** 支付类型
     * 类型名不要包含数字，避免WechatPayInfo出现
     * type+entityId有相同的情况
     * eg:
     *  type=feiyongjiesuan entityId = 11 type+entityId = feiyongjiesuan11
     *  type=feiyongjiesuan1 entityId = 1 type+entityId = feiyongjiesuan11*/
    tixian("推手提现"),

    test("测试"),

    ;

    private String zhName;

    PayType(String zhName){
        this.zhName = zhName;
    }

    public String getZhName() {
        return this.zhName;
    }

}
