package com.juan.shop.order.goodsearning;

import com.juan.shop.order.IOrderService;
import com.juan.shop.order.event.OrderGoodsChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author guanhuan_li
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OrderGoodsChangeListener implements ApplicationListener<OrderGoodsChangeEvent> {

    @Autowired
    private IOrderService orderService;

    @Override
    @Async
    public void onApplicationEvent(OrderGoodsChangeEvent event) {

        log.info("收到订单变更事件");
        orderService.countEarning(event.getOrderGoodsId());

    }
}
