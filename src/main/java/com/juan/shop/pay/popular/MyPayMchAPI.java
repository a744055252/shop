package com.juan.shop.pay.popular;

import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import utils.StringUtils;
import weixin.popular.api.PayMchAPI;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @author guanhuan_li
 */
@Slf4j
public class MyPayMchAPI extends PayMchAPI {

    protected static ThreadLocal<Boolean> sandboxnew = new ThreadLocal<>();

    protected static final String FRAUD_MCH_URI = "https://fraud.mch.weixin.qq.com";

    protected static String publicKey;

    public static void initPublicKey(String publicPath) {
        // 初始化，获取公钥

//        GetPublickey getPublickey = new GetPublickey();
//        getPublickey.setMch_id(mchid);
//        getPublickey.setNonce_str("" + IdUtils.genId());
//        getPublickey.setSign_type("MD5");
//        GetPublickeyResult getPublickeyResult = riskGetpublickey(getPublickey, key);
//        publicKey = StringUtil.substringBetween(getPublickeyResult.getPub_key(),
//                "-----BEGIN RSA PUBLIC KEY-----", "-----END RSA PUBLIC KEY-----");
//
//        log.info("微信公钥：\r\n" + publicKey);

        String publicKey;
        try {
            publicKey = FileUtils.readFileToString(new File(publicPath), Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("读取微信公钥错误！", e);
            throw LogicException.valueOfUnknow(e.getMessage());
        }
        MyPayMchAPI.publicKey = StringUtils.substringBetween(publicKey,
                "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        log.info("微信公钥：\r\n" +  MyPayMchAPI.publicKey);
    }


    /**
     * 查询企业付款
     * @param bank
     * @param key
     * @return
     */
    public static QueryBankResult mmpaysptransQueryBank(QueryBank bank, String key) {
        Map<String, String> map = MapUtil.objectToMap(bank);
        String sign = SignatureUtil.generateSign(map, "MD5", key);
        bank.setSign(sign);
        String secapiPayRefundXML = XMLConverUtil.convertToXML(bank);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(xmlHeader)
                .setUri(baseURI() + "/mmpaysptrans/query_bank")
                .setEntity(new StringEntity(secapiPayRefundXML, Charset.forName("utf-8")))
                .build();

        return LocalHttpClient.keyStoreExecuteXmlResult(bank.getMch_id(), httpUriRequest,
                QueryBankResult.class, "MD5", key);
    }

    /**
     * 企业付款 <br>
     * 付款到银行卡
     * <p>
     * 规则：
     * 单商户日限额——单日10万元
     * 单次限额——单次2万元
     * 单商户给同一银行卡单日限额——单日2万元
     *
     * @param payBank payBank
     * @param key       key
     * @return TransfersResult
     */
    public static PayBankResult mmpaysptransPaybank(PayBank payBank, String key) {

        if (StringUtils.isBlank(publicKey)) {
            throw LogicException.valueOfUnknow("请先加载公钥");
        }

        // 加密
        String enc_bank_no;
        String enc_true_name;
        try {
            enc_bank_no = encrypt(payBank.getEnc_bank_no(), publicKey);
            enc_true_name = encrypt(payBank.getEnc_true_name(), publicKey);
        } catch (Exception e) {
            log.error("加密出错请检查！", e);
            throw LogicException.valueOfUnknow("加密出错请检查！");
        }
        payBank.setEnc_bank_no(enc_bank_no);
        payBank.setEnc_true_name(enc_true_name);


        Map<String, String> map = MapUtil.objectToMap(payBank);
        String sign = SignatureUtil.generateSign(map, "MD5", key);
        payBank.setSign(sign);
        String secapiPayRefundXML = XMLConverUtil.convertToXML(payBank);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(xmlHeader)
                .setUri(baseURI() + "/mmpaysptrans/pay_bank")
                .setEntity(new StringEntity(secapiPayRefundXML, Charset.forName("utf-8")))
                .build();

        return LocalHttpClient.keyStoreExecuteXmlResult(payBank.getMch_id(), httpUriRequest,
                PayBankResult.class, "MD5", key);
    }

    public static GetPublickeyResult riskGetpublickey(GetPublickey getPublickey, String key) {
        Map<String, String> map = MapUtil.objectToMap(getPublickey);
        String sign = SignatureUtil.generateSign(map, getPublickey.getSign_type(), key);
        getPublickey.setSign(sign);
        String secapiPayRefundXML = XMLConverUtil.convertToXML(getPublickey);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(xmlHeader)
                .setUri(fraudBaseURI() + "/risk/getpublickey")
                .setEntity(new StringEntity(secapiPayRefundXML, Charset.forName("utf-8")))
                .build();

        return LocalHttpClient.keyStoreExecuteXmlResult(getPublickey.getMch_id(), httpUriRequest,
                GetPublickeyResult.class, getPublickey.getSign_type(), key);
    }


    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    public static void main(String[] args) {
        String str = "-----BEGIN RSA PUBLIC KEY-----\n" +
                "MIIBCgKCAQEAyigUK7+xPAz6NTrcXlorq6lBrUcTY6aGYmdCgUEBS3BuBrukH5MI\n" +
                "oneilpGHKXlaPWmhjxKrzq6zo/oEarYSEWxjGidJhk0b7GmYlyHPcUHalUbqNzw/\n" +
                "jjOeRbp3Rvy7RasY67vIoTAan/phqG/oWmq5IOfycZd9w9NcbqTRxc4KCJfMSJQx\n" +
                "O63QG6lhE3y+mQaFcHiHSS9MY6pQqhcsc9G44mWSzPAMpjWtD3vPuZvebMrIUkXS\n" +
                "Qm1oL2wnHRRRzLvYE8tVqxFxLupR4lp/Ril1mYwF2ijWTUZ9BZWrueXiAyxubdBP\n" +
                "oiptPgfQVbTH7VpdOAbCga4HOisSE5eDtQIDAQAB\n" +
                "-----END RSA PUBLIC KEY-----";
        String s = StringUtils.substringBetween(str, "-----BEGIN RSA PUBLIC KEY-----", "-----END RSA PUBLIC KEY-----");
        System.out.println(s);
    }


    /**
     * 仿真测试 开始
     * @since 2.8.6
     */
    public static void sandboxnewStart(){
        sandboxnewStart();
        sandboxnew.set(true);
    }

    /**
     * 仿真测试 结束
     * @since 2.8.6
     */
    public static void mySandboxnewEnd(){
        sandboxnewEnd();
        sandboxnew.set(null);
    }

    /**
     * 获取支付base URI路径
     * @return baseURI
     */
    private static String baseURI(){
        if(sandboxnew.get() == null){
            return MCH_URI;
        }else{
            return MCH_URI + "/sandboxnew";
        }
    }

    private static String fraudBaseURI() {
        if(sandboxnew.get() == null){
            return FRAUD_MCH_URI;
        }else{
            return FRAUD_MCH_URI + "/sandboxnew";
        }
    }

}
