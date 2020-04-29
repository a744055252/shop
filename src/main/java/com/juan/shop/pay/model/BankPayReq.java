package com.juan.shop.pay.model;

import com.juan.shop.pay.model.Bank;
import com.juan.shop.pay.model.PayType;

/**
 * @author guanhuan_li
 */
public class BankPayReq {

    private PayType type;

    /** 对应的实体id */
    private Long entityId;

    /**
     * 收款方银行卡号 (传入时不需要加密)
     *
     */
    private String bankNo;

    /**
     * 收款方用户名 (传入时不需要加密)
     *
     * */
    private String trueName;

    /**
     * 收款方开户行
     * */
    private Bank bank;

    /**
     * 付款金额
     * RMB分（支付总额，不含手续费）
     * 注：大于0的整数
     *
     * */
    private Integer amount;

    /**
     * 企业付款到银行卡付款说明,即订单备注（UTF8编码，允许100个字符以内）
     */
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

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
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
