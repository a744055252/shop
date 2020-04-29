package com.juan.shop.pay;

import com.juan.shop.pay.model.BankPayReq;
import com.juan.shop.pay.model.PayReq;
import com.juan.shop.pay.model.PayStatus;
import com.juan.shop.pay.model.PayType;
import com.juan.shop.pay.popular.*;
import com.juan.shop.spring.WechatAuthProperties;
import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import util.IdUtils;
import util.JsonUtils;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.Transfers;
import weixin.popular.bean.paymch.TransfersResult;
import weixin.popular.client.LocalHttpClient;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 使用前，先添加证书及相关参数
 * @author guanhuan_li
 */
@Slf4j
//@Component
public class WechatPayService implements IWechatPayService {

    /** 支付到银行卡付款单号前缀 */
    private static final String BAN_PARTNER_PRE = "BANK";

    /** 支付到银行卡付款单号前缀 */
    private static final String LING_PARTNER_PRE = "LING";

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    /** 商户账号appid */
    private String mch_appid;

    /** 商户号 */
    private String mchid;

    private String ip;

    private String key;

    @Autowired
    private WechatBankPayInfoRepository wechatBankPayInfoRepository;

    @Autowired
    private WechatPayInfoRepository wechatPayInfoRepository;

    @PostConstruct
    public void init(){

        this.mch_appid = wechatAuthProperties.getMchAppid();
        this.mchid = wechatAuthProperties.getMchid();
        this.ip = wechatAuthProperties.getIp();
        this.key = wechatAuthProperties.getKey();

        log.info("微信支付服务启动！");
        log.info("商户账号appid:" + mch_appid);
        log.info("商户号:" + mchid);
        log.info("key:" + key);

        log.info("加载证书！");

        String classpath = this.getClass().getResource("/").getPath();
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String certPath = webappRoot + "cert/apiclient_cert.p12";
        log.info("证书位置：" + certPath);

        // 加载公钥
        String publicPath = webappRoot + "cert/RSA_Public.pem";
        MyPayMchAPI.initPublicKey(publicPath);
        log.info("公钥位置：" + publicPath);

        LocalHttpClient.initMchKeyStore(mchid, certPath);
    }

    /**
     * 一个 {@link PayType} 和 entityId 对应一个支付信息，多次调用pay只会支付一次
     *
     * @param req
     * @return
     */
    @Override
    public synchronized WechatPayInfo pay(PayReq req) {

        Date now = new Date();
        WechatPayInfo info = getByEntity(req.getType(), req.getEntityId());
        if (info == null) {
            info = new WechatPayInfo();
            info.setCreateDate(now);
            convert(req, info);

            // 商户账号appid
            info.setMch_appid(mch_appid);
            // 商户号
            info.setMchid(mchid);

            String nonceStr = "" + IdUtils.genId();
            info.setNonce_str(nonceStr);
            info.setSpbill_create_ip(ip);
            info.setPartner_trade_no(LING_PARTNER_PRE+req.getType().name() + req.getEntityId());
        }

        Transfers transfers = new Transfers();
        convert(info, transfers);

        //todo 测试阶段转账为0.3元
//        transfers.setDesc(transfers.getDesc() + " 实际金额(分)："+transfers.getAmount());
//        transfers.setAmount(30);
        TransfersResult result = null;
        try {
            result = PayMchAPI.mmpaymkttransfersPromotionTransfers(transfers, key);
        } finally {

            if (result != null) {
                convert(result, info);
                final String SUCCESS = "SUCCESS";
                if (SUCCESS.equals(result.getResult_code())) {
                    // 支付成功
                    info.setStatus(PayStatus.SUCCESS);
                } else {
                    info.setStatus(PayStatus.FAILED);
                }
            } else {
                info.setStatus(PayStatus.ERROR);
            }

            info.setUpdateDate(now);

            wechatPayInfoRepository.save(info);
            log.info("更新微信支付：" + JsonUtils.encodeJson(info));
        }

        return info;
    }

    @Override
    @Scheduled(cron = " 0 0 1 * * ? ")
    public void task() {

        // 付款到银行卡
        Date now = new Date();
        List<WechatBankPayInfo> wechatBankPayInfos = listAllProcessingBank();
        int count = 0;
        for (WechatBankPayInfo wechatBankPayInfo : wechatBankPayInfos) {
            QueryBank queryBank = new QueryBank();
            queryBank.setMch_id(mchid);
            queryBank.setNonce_str(""+IdUtils.genId());
            queryBank.setPartner_trade_no(wechatBankPayInfo.getPartner_trade_no());
            QueryBankResult queryBankResult = MyPayMchAPI.mmpaysptransQueryBank(queryBank, key);
            // 查询成功
            final String SUCCESS = "SUCCESS";
            if (SUCCESS.equals(queryBankResult.getReturn_code()) && SUCCESS.equals(queryBankResult.getResult_code())) {

                count++;

                PayStatus payStatus = PayStatus.valueOf(queryBankResult.getStatus());
                wechatBankPayInfo.setStatus(payStatus);
                wechatBankPayInfo.setUpdateDate(now);

                log.info("支付到银行卡订单："+wechatBankPayInfo.getId()+" 状态变更为["+payStatus.getZhName()+"]");
            } else {
                wechatBankPayInfo.setReturn_msg(queryBankResult.getReason());
                wechatBankPayInfo.setUpdateDate(now);
            }

        }
        wechatBankPayInfoRepository.saveAll(wechatBankPayInfos);
        log.info("查询订单数量："+wechatBankPayInfos.size()+", 订单成功数量："+count);
    }

    @Override
    public synchronized WechatBankPayInfo bankPay(BankPayReq req){

        Date now = new Date();
        WechatBankPayInfo info = getBankByEntity(req.getType(), req.getEntityId());
        if (info == null) {
            info = new WechatBankPayInfo();
            info.setCreateDate(now);
            // 商户号
            info.setMchid(mchid);
            info.setAmount(req.getAmount());
            info.setBank_code(req.getBank().getCode());
            info.setBank_desc(req.getDesc());
            info.setEnc_bank_no(req.getBankNo());
            info.setEnc_true_name(req.getTrueName());
            info.setEntityId(req.getEntityId());
            info.setType(req.getType());
            String nonceStr = "" + IdUtils.genId();
            info.setNonce_str(nonceStr);
            info.setPartner_trade_no(BAN_PARTNER_PRE+req.getType().name() + req.getEntityId());
        }

        PayBank payBank = new PayBank();
        convert(info, payBank);

        //todo 测试阶段转账为0.3元
        payBank.setDesc(payBank.getDesc() + " 实际金额："+ payBank.getAmount());
        payBank.setAmount(30);
        PayBankResult result = null;
        try {
            result = MyPayMchAPI.mmpaysptransPaybank(payBank, key);
        } finally {

            if (result != null) {
                convert(result, info);
            }

            info.setUpdateDate(now);

            wechatBankPayInfoRepository.save(info);
            log.info("修改微信支付：" + JsonUtils.encodeJson(info));
        }

        return info;
    }

    /**
     * 获取所有处理中的企业支付到零钱的订单
     * @return
     */
    private List<WechatPayInfo> listAllProcessingLing(){
        return wechatPayInfoRepository.findByStatus(PayStatus.PROCESSING);
    }

    /**
     * 获取所有处理中的企业支付到银行卡的订单
     * @return
     */
    private List<WechatBankPayInfo> listAllProcessingBank(){
        return wechatBankPayInfoRepository.findByStatus(PayStatus.PROCESSING);
    }

    private WechatBankPayInfo getBankByEntity(PayType type, Long entityId) {

        Optional<WechatBankPayInfo> infoOpt = wechatBankPayInfoRepository.findByTypeAndEntityId(type, entityId);
        return infoOpt.orElseThrow(() ->
                LogicException.valueOfUnknow("不存在类型为：" + type.getZhName() + " 实体：" + entityId + " 的支付信息"));
    }


    private WechatPayInfo getByEntity(PayType type, Long entityId) {
        Optional<WechatPayInfo> infoOpt = wechatPayInfoRepository.findByTypeAndEntityId(type, entityId);
        return infoOpt.orElseThrow(() ->
                LogicException.valueOfUnknow("不存在类型为：" + type.getZhName() + " 实体：" + entityId + " 的支付信息"));
    }

    private static void convert(PayBankResult source, WechatBankPayInfo target) {
        target.setReturn_code(source.getReturn_code());
        target.setReturn_msg(source.getReturn_msg());
        target.setErr_code(source.getErr_code());
        target.setErr_code_des(source.getErr_code_des());
        target.setPayment_no(source.getPayment_no());
    }

    private static void convert(WechatBankPayInfo source, PayBank target) {
        target.setMch_id(source.getMchid());
        target.setPartner_trade_no(source.getPartner_trade_no());
        target.setNonce_str(source.getNonce_str());
        target.setSign(source.getSign());
        target.setEnc_bank_no(source.getEnc_bank_no());
        target.setEnc_true_name(source.getEnc_true_name());
        target.setBank_code(source.getBank_code());
        target.setAmount(source.getAmount());
        target.setDesc(source.getBank_desc());
    }

    private static void convert(TransfersResult source, WechatPayInfo target) {
        target.setReturn_code(source.getReturn_code());
        target.setReturn_msg(source.getReturn_msg());
        target.setErr_code(source.getErr_code());
        target.setErr_code_des(source.getErr_code_des());
        target.setPayment_no(source.getPayment_no());
        target.setPayment_time(source.getPayment_time());
    }

    private static void convert(PayReq source, WechatPayInfo target) {
        target.setType(source.getType());
        target.setEntityId(source.getEntityId());
        target.setOpenid(source.getOpenid());
        target.setCheck_name(source.getCheck_name().name());
        target.setRe_user_name(source.getRe_user_name());
        target.setAmount(source.getAmount());
        target.setQiye_desc(source.getDesc());
    }


    private static void convert(WechatPayInfo source, Transfers target) {
        target.setMch_appid(source.getMch_appid());
        target.setMchid(source.getMchid());
        target.setDevice_info(source.getDevice_info());
        target.setNonce_str(source.getNonce_str());
        target.setPartner_trade_no(source.getPartner_trade_no());
        target.setOpenid(source.getOpenid());
        target.setCheck_name(source.getCheck_name());
        target.setRe_user_name(source.getRe_user_name());
        target.setAmount(source.getAmount());
        target.setDesc(source.getQiye_desc());
        target.setSpbill_create_ip(source.getSpbill_create_ip());
    }

}
