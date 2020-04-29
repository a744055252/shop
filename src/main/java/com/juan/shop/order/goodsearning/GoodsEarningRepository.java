package com.juan.shop.order.goodsearning;

import com.juan.shop.order.model.OrderStatus;
import com.juan.shop.user.wallet.model.WithdrawStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface GoodsEarningRepository extends JpaRepository<GoodsEarning, Long> {

    Optional<GoodsEarning> findByOrderGoodsId(Long orderGoodsId);

    List<GoodsEarning> findByUserIdAndWithdrawStatusAndStatus(Long userId, WithdrawStatus withdrawStatus, OrderStatus orderStatus);

}
