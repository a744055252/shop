package com.juan.shop.pay.popular;

import weixin.popular.bean.paymch.MchBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author guanhuan_li
 */
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayBankResult extends MchBase {

    /** 商户企业付款单号 */
    private String partner_trade_no;

    /** 代付金额 */
    private Integer amount;

    /** 代付成功后，返回的内部业务单号 */
    private String payment_no;

    /** 手续费金额 RMB：分 */
    private Integer cmms_amt;

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPayment_no() {
        return payment_no;
    }

    public void setPayment_no(String payment_no) {
        this.payment_no = payment_no;
    }

    public Integer getCmms_amt() {
        return cmms_amt;
    }

    public void setCmms_amt(Integer cmms_amt) {
        this.cmms_amt = cmms_amt;
    }
}
