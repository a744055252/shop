package com.juan.shop.pay.popular;

/**
 * @author guanhuan_li
 */
public class QueryBank {
    /** 商户号 */
    private String mch_id;

    /**
     *  商户企业付款单号
     * 商户订单号，需保持唯一（只允许数字[0~9]或字母[A~Z]和[a~z]，最短8位，最长32位）
     *
     * */
    private String partner_trade_no;

    /** 随机字符串，不长于32位 */
    private String nonce_str;

    /** 签名 */
    private String sign;

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
