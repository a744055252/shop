package com.juan.shop.order.model;

import com.juan.shop.three.jd.Platform;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author guanhuan_li
 */
@Data
public class ListOrderRes {

    /** 平台订单id */
    private Long orderId;

    /** 订单所属平台 */
    private Platform platform;

    /** 订单完成时间 */
    private Date finishDate;

    /** 下单设备(1:PC,2:无线) */
    private Integer orderEmt;

    /** 下单时间 */
    private Date orderDate;

    /** 父订单id */
    private Long parentId;

    /** 订单维度预估结算时间 yyyyMMdd */
    private Date payMonth;

    /** 是否是会员下单 */
    private Boolean flag;

    /** 订单中的商品 */
    private List<OrderGoodsRes> orderGoodsRes;

    @Data
    public static class OrderGoodsRes{
        /** 商品第三方id */
        private Long skuId;

        /** 实际计算佣金的金额。订单完成后，会将误扣除的运费券金额更正。如订单完成后发生退款，此金额会更新。 */
        private Double actualCosPrice;

        /** 推客获得的实际佣金（实际计佣金额*佣金比例*最终比例）。如订单完成后发生退款，此金额会更新。 */
        private Double actualFee;

        /** 佣金比例 */
        private Double commissionRate;

        /** 预估计计算佣金的金额 */
        private Double estimateCosPrice;

        /** 预估计佣金额，即用户下单的金额(已扣除优惠券、白条、支付优惠、进口税，未扣除红包和京豆)，
         * 有时会误扣除运费券金额，完成结算时会在实际计佣金额中更正。如订单完成前发生退款，此金额也会更新。 */
        private Double estimateFee;

        /** 最终比例（分成比例+补贴比例） */
        private Double finalRate;

        /** 商品数量 */
        private Long skuNum;

        /** 商品已退货数量 */
        private Long skuReturnNum;

        /** 商品名 */
        private String name;

        /** 商品单价 */
        private Double price;

        /** 分成比例 */
        private Double subSideRate;

        /** 补贴比例 */
        private Double subsidyRate;

        /** 一级类目id */
        private Long cid1;

        private String cid1Name;

        /** 二级类目id */
        private Long cid2;

        private String cid2Name;

        /** 三级类目id */
        private Long cid3;

        private String cid3Name;

        /** PID所属母账号平台名称（原第三方服务商来源）*/
        private String unionAlias;

        /** 联盟标签数据 */
        private String unionTag;

        /** 渠道组 1：1号店，其他：京东 */
        private Integer unionTrafficGroup;

        /** sku维度的有效码（-1：未知,2.无效-拆单,3.无效-取消,
         * 4.无效-京东帮帮主订单,5.无效-账号异常,6.无效-赠品类目不返佣,
         * 7.无效-校园订单,8.无效-企业订单,9.无效-团购订单,10.无效-开增值税专用发票订单,
         * 11.无效-乡村推广员下单,12.无效-自己推广自己下单,13.无效-违规订单,
         * 14.无效-来源与备案网址不符,15.待付款,16.已付款,17.已完成,18.已结算（5.9号不再支持结算状态回写展示）） */
        private Integer validCode;

        /** 子联盟ID */
        private String subUnionId;

        /** 2：同店；3：跨店 */
        private Integer traceType;

        /** 订单行维度预估结算时间 */
        private Date payMonth;

        /** 商家ID。'订单行维度' */
        private Long popId;

        /** 推客生成推广链接时传入的扩展字段 */
        private String ext1;

        /** 招商团活动id，正整数，为0时表示无活动 */
        private Long cpActId;

        /** 站长角色，1： 推客、 2： 团长 */
        private Integer unionRole;

        /** 礼金批次ID */
        private String giftCouponKey;

        /** 礼金分摊金额 */
        private Double giftCouponOcsAmount;
    }

}
