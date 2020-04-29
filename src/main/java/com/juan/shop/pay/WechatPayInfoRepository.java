package com.juan.shop.pay;

import com.juan.shop.pay.model.PayStatus;
import com.juan.shop.pay.model.PayType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface WechatPayInfoRepository extends JpaRepository<WechatPayInfo, Long> {

    List<WechatPayInfo> findByStatus(PayStatus status);

    Optional<WechatPayInfo> findByTypeAndEntityId(PayType type, Long entityId);

}
