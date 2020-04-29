package com.juan.shop.pay;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.pay.model.PayStatus;
import com.juan.shop.pay.model.PayType;

import javax.persistence.*;

/**
 * @author guanhuan_li
 */
@Entity
public class WechatPayInfo extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayType type;

    @Enumerated(EnumType.STRING)
    private PayStatus status;

    /** 对应的实体id */
    private Long entityId;

    /** 商户账号appid */
    private String mch_appid;

    /** 商户号 */
    private String mchid;

    /** 设备号 */
    private String device_info;

    /** 随机字符串 */
    private String nonce_str;

    /** 商户订单号
     * 使用type+entityId作为唯一的商户订单号
     * */
    private String partner_trade_no;

    /** 用户openid */
    private String openid;

    /** 校验用户姓名选项 */
    private String check_name;

    /** 收款用户姓名 */
    private String re_user_name;

    /** 金额 */
    private Integer amount;

    /** 企业付款备注 */
    private String qiye_desc;

    /** Ip地址 */
    private String spbill_create_ip;

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

    public Long getId() {
        return id;
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

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
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

    public String getQiye_desc() {
        return qiye_desc;
    }

    public void setQiye_desc(String qiye_desc) {
        this.qiye_desc = qiye_desc;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
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
}
