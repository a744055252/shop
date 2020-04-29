package com.juan.shop.order;

import com.juan.shop.common.PageRes;
import com.juan.shop.order.goodsearning.GoodsEarning;
import com.juan.shop.order.model.ListOrderReq;
import com.juan.shop.order.model.ListOrderRes;

import java.util.List;

/**
 * @author guanhuan_li
 */
public interface IOrderService {

    void countEarning(Long orderGoodsId);

    PageRes<ListOrderRes> page(ListOrderReq req, Long operId);

    /**
     * 获取用户可以结算的收益清单
     * @param userId
     * @return
     */
    List<GoodsEarning> findWithdraw(Long userId);

    void saveAllGoodsEarning(List<GoodsEarning> goodsEarnings);

    void checker();
}
