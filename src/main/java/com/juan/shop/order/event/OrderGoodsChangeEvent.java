package com.juan.shop.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author guanhuan_li
 */
@Getter
public class OrderGoodsChangeEvent extends ApplicationEvent {

    private Long orderGoodsId;

    public OrderGoodsChangeEvent(Long orderGoodsId) {
        super(orderGoodsId);
        this.orderGoodsId = orderGoodsId;
    }
}
