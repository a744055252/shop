package com.juan.shop.pay.model;

import com.juan.shop.pay.model.CheckType;
import com.juan.shop.pay.model.PayType;

/**
 * @author guanhuan_li
 */
public class PayReq {

    private PayType type;

    /** 对应的实体id */
    private Long entityId;

    /** 用户openid */
    private String openid;

    /** 校验用户姓名选项 */
    private CheckType check_name;

    /** 收款用户姓名 */
    private String re_user_name;

    /** 金额 单位为分*/
    private Integer amount;

    /** 企业付款备注 */
    private String desc;

    public PayType getType() {
        return type;
    }

    public void setType(PayType type) {
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public CheckType getCheck_name() {
        return check_name;
    }

    public void setCheck_name(CheckType check_name) {
        this.check_name = check_name;
    }

    public String getRe_user_name() {
        return re_user_name;
    }

    public void setRe_user_name(String re_user_name) {
        this.re_user_name = re_user_name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
