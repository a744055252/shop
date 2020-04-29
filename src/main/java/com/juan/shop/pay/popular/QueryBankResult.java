package com.juan.shop.pay.popular;

import weixin.popular.bean.paymch.MchBase;

/**
 * @author guanhuan_li
 */
public class QueryBankResult extends MchBase {

    /** 商户企业付款单号 */
    private String partner_trade_no;

    /** 微信企业付款单号 */
    private String payment_no;

    /** 银行卡号	MD5加密 */
    private String bank_no_md5;

    /** 用户真实姓名 MD5加密 */
    private String true_name_md5;

    /** 代付金额 */
    private Integer amount;

    /**
     * 代付订单状态：
     * PROCESSING（处理中，如有明确失败，则返回额外失败原因；否则没有错误原因）
     * SUCCESS（付款成功）
     * FAILED（付款失败,需要替换付款单号重新发起付款）
     * BANK_FAIL（银行退票，订单状态由付款成功流转至退票,退票时付款金额和手续费会自动退还）
     */
    private String status;

    /** 手续费金额(分) */
    private Integer cmms_amt;

    /** 商户下单时间 */
    private String create_time;

    /** 成功付款时间 */
    private String pay_succ_time;

    /** 失败原因 */
    private String reason;

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getPayment_no() {
        return payment_no;
    }

    public void setPayment_no(String payment_no) {
        this.payment_no = payment_no;
    }

    public String getBank_no_md5() {
        return bank_no_md5;
    }

    public void setBank_no_md5(String bank_no_md5) {
        this.bank_no_md5 = bank_no_md5;
    }

    public String getTrue_name_md5() {
        return true_name_md5;
    }

    public void setTrue_name_md5(String true_name_md5) {
        this.true_name_md5 = true_name_md5;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCmms_amt() {
        return cmms_amt;
    }

    public void setCmms_amt(Integer cmms_amt) {
        this.cmms_amt = cmms_amt;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_succ_time() {
        return pay_succ_time;
    }

    public void setPay_succ_time(String pay_succ_time) {
        this.pay_succ_time = pay_succ_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
