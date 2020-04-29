package com.juan.shop.order.goodsearning;

import com.juan.shop.common.AbstractEntity;
import com.juan.shop.order.model.OrderStatus;
import com.juan.shop.user.User;
import com.juan.shop.user.wallet.Withdraw;
import com.juan.shop.user.wallet.model.WithdrawStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单收益
 * @author guanhuan_li
 */
@Data
@Entity
public class GoodsEarning extends AbstractEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 订单商品id {@link com.juan.shop.order.ordergoods.OrderGoods#getId()} */
    private Long orderGoodsId;

    /** 拥有人id {@link User#getId()} */
    private Long userId;

    /** 提现id {@link Withdraw#getId()} */
    private Long withdrawId;

    /** 订单状态 */
    private OrderStatus status;

    /** 商品价格 */
    private Double price;

    /** 预估佣金额 */
    private Double estimateCosPrice;

    /** 推客的预估佣金 */
    private Double estimateFee;
    
    /** 实际计算佣金的金额 */
    private Double actualCosPrice;
    
    /** 推客获得的实际佣金 */
    private Double actualFee;

    /** 分配的比例 */
    private Double rate;

    /** 平台获得的佣金 */
    private Double platformFee;
    
    /** 提现状态 */
    private WithdrawStatus withdrawStatus;

    /** sku维度的有效码（-1：未知,2.无效-拆单,3.无效-取消,
     * 4.无效-京东帮帮主订单,5.无效-账号异常,6.无效-赠品类目不返佣,
     * 7.无效-校园订单,8.无效-企业订单,9.无效-团购订单,10.无效-开增值税专用发票订单,
     * 11.无效-乡村推广员下单,12.无效-自己推广自己下单,13.无效-违规订单,
     * 14.无效-来源与备案网址不符,15.待付款,16.已付款,17.已完成,18.已结算（5.9号不再支持结算状态回写展示）） */
    private Integer validCode;


    
}
