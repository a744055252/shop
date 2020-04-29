package com.juan.shop.pay;

import com.juan.shop.pay.model.PayStatus;
import com.juan.shop.pay.model.PayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface WechatBankPayInfoRepository extends JpaRepository<WechatBankPayInfo, Long> {

    List<WechatBankPayInfo> findByStatus(PayStatus status);

    Optional<WechatBankPayInfo> findByTypeAndEntityId(PayType type, Long entityId);

}
