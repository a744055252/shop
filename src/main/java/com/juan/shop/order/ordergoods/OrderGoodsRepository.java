package com.juan.shop.order.ordergoods;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author guanhuan_li
 */
public interface OrderGoodsRepository extends JpaRepository<OrderGoods, Long> {

    Page<OrderGoods> findByPositionId(String positionId, Pageable pageable);

    List<OrderGoods> findByOrderId(Long orderId);
}

