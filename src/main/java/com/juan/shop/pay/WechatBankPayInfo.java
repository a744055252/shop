package com.juan.shop.pay;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.pay.model.PayStatus;
import com.juan.shop.pay.model.PayType;

import javax.persistence.*;

/**
 * @author guanhuan_li
 */
@Entity
public class WechatBankPayInfo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayType type;

    @Enumerated(EnumType.STRING)
    private PayStatus status;

    /** 对应的实体id */
    private Long entityId;

    /** 商户号 */
    private String mchid;

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

    /**
     * 收款方银行卡号 (传入时不需要加密)
     *
     * 采用标准RSA算法，公钥由微信侧提供
     */
    private String enc_bank_no;

    /**
     * 收款方用户名 (传入时不需要加密)
     *
     * 采用标准RSA算法，公钥由微信侧提供
     * */
    private String enc_true_name;

    /**
     * 收款方开户行
     * */
    private String bank_code;

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
    private String bank_desc;

    /** 返回状态码 SUCCESS */
    private String return_code;

    /** 返回信息 */
    private String return_msg;

    /** 错误代码 */
    private String err_code;

    /** 错误代码描述 */
    private String err_code_des;

    /** 微信付款单号 */
    private String payment_no;

    /** 付款成功时间 */
    private String payment_time;

    public PayStatus getStatus() {
        return status;
    }

    public void setStatus(PayStatus status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
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

    public String getEnc_bank_no() {
        return enc_bank_no;
    }

    public void setEnc_bank_no(String enc_bank_no) {
        this.enc_bank_no = enc_bank_no;
    }

    public String getEnc_true_name() {
        return enc_true_name;
    }

    public void setEnc_true_name(String enc_true_name) {
        this.enc_true_name = enc_true_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getBank_desc() {
        return bank_desc;
    }

    public void setBank_desc(String bank_desc) {
        this.bank_desc = bank_desc;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getPayment_no() {
        return payment_no;
    }

    public void setPayment_no(String payment_no) {
        this.payment_no = payment_no;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public Long getId() {
        return this.id;
    }
}
