package com.juan.shop.user.wallet;

import com.juan.shop.order.goodsearning.GoodsEarning;

/**
 * @author guanhuan_li
 */
public interface IWalletService {

    /**
     * 添加金额
     * @param goodsEarning
     */
    void addActual(GoodsEarning goodsEarning);

    void withdraw(Long operId);

    void withdrawById(Long withdrawId);

}
