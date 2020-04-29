package com.juan.shop.three.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.jingfen.query.request.UnionOpenGoodsJingfenQueryRequest;
import jd.union.open.goods.jingfen.query.response.UnionOpenGoodsJingfenQueryResponse;
import jd.union.open.order.bonus.query.request.BonusOrderReq;
import jd.union.open.order.bonus.query.request.UnionOpenOrderBonusQueryRequest;
import jd.union.open.order.bonus.query.response.UnionOpenOrderBonusQueryResponse;
import jd.union.open.order.query.request.OrderReq;
import jd.union.open.order.query.request.UnionOpenOrderQueryRequest;
import jd.union.open.order.query.response.UnionOpenOrderQueryResponse;
import jd.union.open.promotion.common.get.request.PromotionCodeReq;
import jd.union.open.promotion.common.get.request.UnionOpenPromotionCommonGetRequest;
import jd.union.open.promotion.common.get.response.UnionOpenPromotionCommonGetResponse;
import utils.JsonUtils;

/**
 * @author guanhuan_li
 */
public class Test {

    private static String SERVER_URL = "https://router.jd.com/api";

    private static String accessToken = null;

    private static String appKey = "756a7119d08b405ff38fa31860bd0dec";

    private static String appSecret = "e918d4a4d7a247128772b8c937246164";

    public static void main(String[] args) throws JdException {
        api4();
    }

    /** 生成推广链接 */
    private static void api4() throws JdException {
        JdClient client=new DefaultJdClient(SERVER_URL,accessToken,appKey,appSecret);
        UnionOpenPromotionCommonGetRequest request=new UnionOpenPromotionCommonGetRequest();
        PromotionCodeReq promotionCodeReq=new PromotionCodeReq();

//        promotionCodeReq.setMaterialId("item.jd.com/3337468.html");
        promotionCodeReq.setMaterialId("item.jd.com/100002040431.html");
        promotionCodeReq.setSiteId("1998094659");
        // 推广位
        promotionCodeReq.setPositionId(2002530690L);

        request.setPromotionCodeReq(promotionCodeReq);
        UnionOpenPromotionCommonGetResponse response=client.execute(request);
        System.out.println(response.getData().getClickURL());
    }

    /** 获取商品列表 */
    private static void api3() throws JdException {
        JdClient client=new DefaultJdClient(SERVER_URL,accessToken,appKey,appSecret);
        UnionOpenGoodsJingfenQueryRequest request = new UnionOpenGoodsJingfenQueryRequest();
        JFGoodsReq goodsReq = new JFGoodsReq();
        goodsReq.setPageSize(10);
        goodsReq.setPageIndex(1);
        goodsReq.setEliteId(1);
        request.setGoodsReq(goodsReq);
        UnionOpenGoodsJingfenQueryResponse response=client.execute(request);
        System.out.println(JsonUtils.encodeJson(response));
    }

    /** 查询推广结果 */
    private static void api1() throws JdException {
        JdClient client=new DefaultJdClient(SERVER_URL,accessToken,appKey,appSecret);
        UnionOpenOrderQueryRequest request=new UnionOpenOrderQueryRequest();
        OrderReq orderReq=new OrderReq();
        orderReq.setPageSize(10);
        orderReq.setPageNo(1);
//        orderReq.setType(2);
//        orderReq.setTime("202001041303");
        orderReq.setType(1);
        orderReq.setTime("20200120181214");
        request.setOrderReq(orderReq);
        UnionOpenOrderQueryResponse response=client.execute(request);
        System.out.println(JsonUtils.encodeJson(response));
    }

    private static void api2() throws JdException {
        JdClient client=new DefaultJdClient(SERVER_URL,accessToken,appKey,appSecret);
        UnionOpenOrderBonusQueryRequest request=new UnionOpenOrderBonusQueryRequest();
        BonusOrderReq orderReq=new BonusOrderReq();
        request.setOrderReq(orderReq);
        UnionOpenOrderBonusQueryResponse response=client.execute(request);
    }
}
