package com.juan.shop.order;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.juan.shop.common.PageRes;
import com.juan.shop.global.GlobalKey;
import com.juan.shop.global.IGlobalService;
import com.juan.shop.order.goodsearning.GoodsEarning;
import com.juan.shop.order.goodsearning.GoodsEarningRepository;
import com.juan.shop.order.model.ListOrderReq;
import com.juan.shop.order.model.ListOrderRes;
import com.juan.shop.order.model.OrderStatus;
import com.juan.shop.order.ordergoods.OrderGoods;
import com.juan.shop.order.ordergoods.OrderGoodsRepository;
import com.juan.shop.order.ordertask.OrderTask;
import com.juan.shop.order.ordertask.OrderTaskRepository;
import com.juan.shop.three.jd.JdManager;
import com.juan.shop.three.jd.Platform;
import com.juan.shop.user.IUserService;
import com.juan.shop.user.User;
import com.juan.shop.user.tier.Tier;
import com.juan.shop.user.wallet.model.WithdrawStatus;
import exception.LogicException;
import jd.union.open.order.query.request.OrderReq;
import jd.union.open.order.query.request.UnionOpenOrderQueryRequest;
import jd.union.open.order.query.response.OrderResp;
import jd.union.open.order.query.response.SkuInfo;
import jd.union.open.order.query.response.UnionOpenOrderQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import util.CodeUtils;
import util.DateUtils;
import util.JsonUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author guanhuan_li
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderGoodsRepository orderGoodsRepository;

    @Autowired
    private OrderTaskRepository orderTaskRepository;

    @Autowired
    private GoodsEarningRepository goodsEarningRepository;

    @Autowired
    private IGlobalService globalService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JdManager jdManager;

//    @PostConstruct
    public void init(){
        checker();
    }

    @Override
    public void countEarning(Long orderGoodsId) {

        Date now = new Date();

        Optional<OrderGoods> ordergoodsOpt = orderGoodsRepository.findById(orderGoodsId);
        if (!ordergoodsOpt.isPresent()) {
            log.info("订单商品id：{}，数据不存在！", orderGoodsId);
            return;
        }

        OrderGoods orderGoods = ordergoodsOpt.get();

        Double commissionRate = Double.valueOf(globalService.getValue(GlobalKey.commissionRate));
        Optional<GoodsEarning> goodsEarningOpt = goodsEarningRepository.findByOrderGoodsId(orderGoods.getId());
        GoodsEarning goodsEarning = goodsEarningOpt.orElseGet(() -> {
            GoodsEarning temp = new GoodsEarning();
            temp.setWithdrawStatus(WithdrawStatus.weitixian);

            // 订单所属人
            String positionId = orderGoods.getPositionId();
            User user = userService.findByPositionId(positionId);
            temp.setUserId(user.getId());

            temp.setOrderGoodsId(orderGoods.getId());
            temp.setCreateDate(now);
            return temp;
        });

        goodsEarning.setUpdateDate(now);

        // 预计收益
        goodsEarning.setEstimateCosPrice(orderGoods.getEstimateFee());
        goodsEarning.setEstimateFee(orderGoods.getEstimateFee() * commissionRate);

        switch (orderGoods.getStatus()) {
            case wuxiao:
                goodsEarning.setEstimateCosPrice(0D);
                goodsEarning.setEstimateFee(0D);
                goodsEarning.setActualCosPrice(0D);
                goodsEarning.setActualFee(0D);
                goodsEarning.setPlatformFee(0D);
                break;
            case daifukuan:
            case yifukuan:
            case yiwancheng:
                // 预计收益
                goodsEarning.setEstimateCosPrice(orderGoods.getEstimateFee());
                goodsEarning.setEstimateFee(orderGoods.getEstimateFee() * commissionRate);
                break;
            case yijiesuan:
                // 实际收益
                // 推手收益
                double customerEarning = orderGoods.getActualFee() * commissionRate;
                // 平台收益
                double platformEarning = orderGoods.getActualFee() - customerEarning;

                goodsEarning.setEstimateCosPrice(orderGoods.getEstimateFee());
                goodsEarning.setEstimateFee(orderGoods.getEstimateFee() * commissionRate);
                goodsEarning.setActualCosPrice(orderGoods.getActualCosPrice());
                goodsEarning.setActualFee(customerEarning);
                goodsEarning.setPlatformFee(platformEarning);
                goodsEarning.setRate(commissionRate);
                break;
            default:
                throw LogicException.valueOfUnknow("不支持的类型");
        }

        goodsEarning.setPrice(orderGoods.getPrice());
        goodsEarning.setValidCode(orderGoods.getValidCode());
        goodsEarning.setStatus(orderGoods.getStatus());

        goodsEarningRepository.save(goodsEarning);

        log.info("更新订单商品id:{} 的佣金", orderGoodsId);
    }

    @Override
    public PageRes<ListOrderRes> page(ListOrderReq req, Long operId) {

        Optional<Tier> tierOpt = userService.getTier(operId);

        if (!tierOpt.isPresent()) {
            return PageRes.valueOf(0, req.getPageSize(), req.getPageNumber(), new ArrayList<>());
        }
        Tier tier = tierOpt.get();

        Page<OrderGoods> orderGoodsPage = orderGoodsRepository.findByPositionId(tier.getPositionId(), req.toPageabel());
        List<OrderGoods> orderGoods = orderGoodsPage.getContent();
        // 订单id和对应的ListOrderRes
        Map<Long, ListOrderRes> id2order = new HashMap<>(orderGoods.size());

        for (OrderGoods orderGood : orderGoods) {
            Long orderId = orderGood.getOrderId();
            ListOrderRes listOrderRes = id2order.get(orderId);
            if (listOrderRes == null) {
                Optional<Order> orderOpt = orderRepository.findById(orderId);
                if (!orderOpt.isPresent()) {
                    log.error("订单商品：{}，的订单不存在", JsonUtils.encodeJson(orderGood));
                    continue;
                }
                listOrderRes = new ListOrderRes();
                Order order = orderOpt.get();
                convert(order, listOrderRes);
                id2order.put(orderId, listOrderRes);
            }

            List<ListOrderRes.OrderGoodsRes> orderGoodsResList = listOrderRes.getOrderGoodsRes();
            if (orderGoodsResList == null) {
                orderGoodsResList = new ArrayList<>();
                listOrderRes.setOrderGoodsRes(orderGoodsResList);
            }

            ListOrderRes.OrderGoodsRes orderGoodsRes = new ListOrderRes.OrderGoodsRes();
            convert(orderGood, orderGoodsRes);

            // 分类 未定义

            orderGoodsResList.add(orderGoodsRes);
        }
        Collection<ListOrderRes> values = id2order.values();
        return PageRes.valueOf(orderGoodsPage, new ArrayList<>(values));
    }

    /**
     * 未提现，已结算的订单才是可以提现的
     * @param userId
     * @return
     */
    @Override
    public List<GoodsEarning> findWithdraw(Long userId) {
        return goodsEarningRepository.findByUserIdAndWithdrawStatusAndStatus(userId,
                WithdrawStatus.weitixian, OrderStatus.yijiesuan);
    }

    @Override
    public void saveAllGoodsEarning(List<GoodsEarning> goodsEarnings) {
        goodsEarningRepository.saveAll(goodsEarnings);
    }

    /**
     * 检查器，检查任务是否完成
     */
    @Scheduled(cron = " 0 44 18 * * ?")
    public void checker(){

        log.info("执行监视器！start");

        Date now = new Date();

        String startDateStr = globalService.getValue(GlobalKey.orderTaskBeginDate);
        Date startDate = DateUtils.truncate(DateUtils.parse(startDateStr, DateUtils.yyyy_MM_dd_HHmmss), Calendar.MINUTE);
//        Date endDate = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
        Date endDate = DateUtils.addMinutes(now, -1);
        // 测试，使用当前时间


        log.info("开始时间:{}，结束时间:{}", DateUtils.format(startDate, DateUtils.yyyy_MM_dd_HHmmss),
                DateUtils.format(endDate, DateUtils.yyyy_MM_dd_HHmmss));

        List<OrderTask> orderTaskList = orderTaskRepository.findByExeDateBetween(startDate, endDate);

        Map<Date, OrderTask> date2task = orderTaskList.stream()
                .collect(Collectors.toMap(OrderTask::getExeDate, Function.identity()));

        OrderTask defaultTask = new OrderTask();
        defaultTask.setFlag(false);

        int sum = 0;
        int fail = 0;
        while (startDate.before(endDate)) {
            sum++;
            // 任务不存在时，默认失败
            OrderTask orderTask = date2task.getOrDefault(startDate, defaultTask);

            // 失败,从新更新
            if (!orderTask.getFlag()) {
                fail++;
                updateTask(startDate);
            }

            // 加1分钟
            startDate = DateUtils.addMinutes(startDate, 1);
        }
        globalService.set(GlobalKey.orderTaskBeginDate, DateUtils.format(endDate, DateUtils.yyyy_MM_dd_HHmmss));

        log.info("检查数量{}, 失败的任务数{}", sum, fail);
        log.info("执行监视器！end");
    }


    /**
     * 每分钟执行
     */
//    @Scheduled(cron = " 0 0/1 * * * ? ")
    public void task(){

        log.info("执行京东获取订单信息定时任务！start");
        
        Date now = new Date();

        Date currentDate = DateUtils.addMinutes(now, -2);

        updateTask(currentDate);

        log.info("执行京东获取订单信息定时任务！end");
    }

    private void updateTask(Date currentDate) {
        String format = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm");
        // 精确到分
        log.info("执行京东获取订单信息任务，时间：{}", format);

        Date nowMinute = DateUtils.truncate(currentDate, Calendar.MINUTE);

        Optional<OrderTask> orderTaskOpt = orderTaskRepository.findByExeDate(nowMinute);
        OrderTask orderTask = orderTaskOpt.orElseGet(() -> {
            OrderTask temp = new OrderTask();
            temp.setFlag(false);
            temp.setCreateDate(currentDate);
            temp.setExeDate(nowMinute);
            return temp;
        });


        JdClient client = jdManager.getClient();

        // 1：下单时间，2：完成时间，3：更新时间
        // 更新 下单时间
        log.info("更新[下单时间]订单！");
        boolean flag1;
        try {
            flag1 = updateOrder(currentDate, client, 1);
        } catch (Exception e) {
            log.error("京东获取[下单时间]订单信息错误！时间：{}", format, e);
            flag1 = false;
        }

        // 更新 完成时间
        log.info("更新[完成时间]订单！");
        boolean flag2;
        try {
            flag2 = updateOrder(currentDate, client, 2);
        } catch (Exception e) {
            log.error("京东获取[完成时间]订单信息错误！时间：{}", format, e);
            flag2 = false;
        }

        // 更新 更新时间
        log.info("更新[更新时间]订单！");
        boolean flag3;
        try {
            flag3 = updateOrder(currentDate, client, 3);
        } catch (Exception e) {
            log.error("京东获取[更新时间]订单信息错误！时间：{}", format, e);
            flag3 = false;
        }


        orderTask.setUpdateDate(currentDate);
        orderTask.setFlag(flag1 && flag2 && flag3);
        orderTaskRepository.save(orderTask);

        log.info("更新订单任务时间:{}, 是否成功:{}", format, orderTask.getFlag());
    }

    private boolean updateOrder(Date now, JdClient client, Integer type) {
        String nowStr = DateUtils.format(now, "yyyyMMddHHmm");
        String format = DateUtils.format(now, "yyyy-MM-dd HH:mm");
        int pageNo = 1;
        while (true) {
            UnionOpenOrderQueryRequest request=new UnionOpenOrderQueryRequest();
            OrderReq orderReq=new OrderReq();
            orderReq.setPageSize(10);
            orderReq.setPageNo(pageNo);
            orderReq.setType(type);
            orderReq.setTime(nowStr);
            request.setOrderReq(orderReq);
            UnionOpenOrderQueryResponse response;
            try {
                response = client.execute(request);
            } catch (JdException e) {
                log.error("京东获取订单信息错误！时间：{}", format);
                return false;
            }

            log.info("结果：{}", JsonUtils.encodeJson(response));

            // 保存订单
            OrderResp[] orderResps = response.getData();

            if (orderResps == null) {
                break;
            }

            for (OrderResp orderResp : orderResps) {
                // 京东平台id
                Long orderId = orderResp.getOrderId();
                Optional<Order> orderOpt = orderRepository.findByPlatformAndPaltformId(Platform.jd, orderId);

                Order order = orderOpt.orElseGet(() -> {
                    Order temp = new Order();
                    temp.setCreateDate(now);
                    temp.setPlatform(Platform.jd);
                    temp.setPaltformId(orderId);
                    return temp;
                });

                order.setUpdateDate(now);
                convert(orderResp, order);

                orderRepository.save(order);

                log.info("更新订单：{}", order.getId());

                // 更新订单中的商品
                List<OrderGoods> orderGoods = orderGoodsRepository.findByOrderId(orderId);
                Map<Long, OrderGoods> orderId2goods = orderGoods.stream()
                        .collect(Collectors.toMap(OrderGoods::getOrderId, Function.identity()));

                SkuInfo[] skuList = orderResp.getSkuList();

                // 更新列表
                List<OrderGoods> orderGoodsList = new ArrayList<>();

                for (SkuInfo skuInfo : skuList) {
                    OrderGoods goods = orderId2goods.get(skuInfo.getSkuId());
                    if (goods == null) {
                        goods = new OrderGoods();
                        goods.setCreateDate(now);
                    }
                    goods.setUpdateDate(now);

                    User user = userService.findByPositionId("" + skuInfo.getPositionId());
                    goods.setUserId(user.getId());
                    goods.setOrderId(order.getId());
                    convert(skuInfo, goods);
                    orderGoodsList.add(goods);
                }

                orderGoodsRepository.saveAll(orderGoodsList);

                log.info("更新订单：{}, 的{}件商品！", order.getId(), orderGoodsList.size());

            }

            Boolean hasMore = response.getHasMore();
            if (!hasMore) {
                // 没有更多，跳出循环
                break;
            }
            pageNo++;
        }

        return true;
    }

    private static void convert(SkuInfo source, OrderGoods target) {

        switch (source.getValidCode()) {
            case 15 :
                target.setStatus(OrderStatus.daifukuan);
                break;
            case 16:
                target.setStatus(OrderStatus.yifukuan);
                break;
            case 17:
                target.setStatus(OrderStatus.yiwancheng);
                break;
            case 18:
                target.setStatus(OrderStatus.yijiesuan);
                break;
            default:
                target.setStatus(OrderStatus.wuxiao);
        }

        target.setName(source.getSkuName());
        target.setPayMonth(new Date(source.getPayMonth()));
        target.setPositionId(""+source.getPositionId());
        target.setSkuId(source.getSkuId());
        target.setActualCosPrice(source.getActualCosPrice());
        target.setActualFee(source.getActualFee());
        target.setCommissionRate(source.getCommissionRate());
        target.setEstimateCosPrice(source.getEstimateCosPrice());
        target.setEstimateFee(source.getEstimateFee());
        target.setFinalRate(source.getFinalRate());
        target.setSkuNum(source.getSkuNum());
        target.setSkuReturnNum(source.getSkuReturnNum());
        target.setPrice(source.getPrice());
        target.setSubSideRate(source.getSubSideRate());
        target.setSubsidyRate(source.getSubsidyRate());
        target.setCid1(source.getCid1());
        target.setCid2(source.getCid2());
        target.setCid3(source.getCid3());
        target.setUnionAlias(source.getUnionAlias());
        target.setUnionTag(source.getUnionTag());
        target.setUnionTrafficGroup(source.getUnionTrafficGroup());
        target.setValidCode(source.getValidCode());
        target.setSubUnionId(source.getSubUnionId());
        target.setTraceType(source.getTraceType());
        target.setPopId(source.getPopId());
        target.setExt1(source.getExt1());
        target.setCpActId(source.getCpActId());
        target.setUnionRole(source.getUnionRole());
        target.setGiftCouponKey(source.getGiftCouponKey());
        target.setGiftCouponOcsAmount(source.getGiftCouponOcsAmount());
    }

    private static void convert(OrderResp source, Order target) {
        target.setPaltformId(source.getOrderId());
        target.setOrderEmt(source.getOrderEmt());
        target.setParentId(source.getParentId());
        // 未结算时返回
        String weijiesuan = "0";
        if (!weijiesuan.equals(source.getPayMonth())) {
            target.setPayMonth(DateUtils.parse(source.getPayMonth(), "yyyyMMdd"));
        }
        target.setUnionId(source.getUnionId());
        target.setExt1(source.getExt1());
        target.setValidCode(source.getValidCode());
        target.setFinishDate(new Date(source.getFinishTime()));
        target.setOrderDate(new Date(source.getOrderTime()));
        target.setFlag(source.getPlus() == 1);
    }

    private static void convert(Order source, ListOrderRes target) {
        target.setPlatform(source.getPlatform());
        target.setFinishDate(source.getFinishDate());
        target.setOrderEmt(source.getOrderEmt());
        target.setOrderDate(source.getOrderDate());
        target.setParentId(source.getParentId());
        target.setPayMonth(source.getPayMonth());
        target.setFlag(source.getFlag());
    }

    private static void convert(OrderGoods source, ListOrderRes.OrderGoodsRes target) {
        target.setSkuId(source.getSkuId());
        target.setActualCosPrice(source.getActualCosPrice());
        target.setActualFee(source.getActualFee());
        target.setCommissionRate(source.getCommissionRate());
        target.setEstimateCosPrice(source.getEstimateCosPrice());
        target.setEstimateFee(source.getEstimateFee());
        target.setFinalRate(source.getFinalRate());
        target.setSkuNum(source.getSkuNum());
        target.setSkuReturnNum(source.getSkuReturnNum());
        target.setName(source.getName());
        target.setPrice(source.getPrice());
        target.setSubSideRate(source.getSubSideRate());
        target.setSubsidyRate(source.getSubsidyRate());
        target.setCid1(source.getCid1());
        target.setCid2(source.getCid2());
        target.setCid3(source.getCid3());
        target.setUnionAlias(source.getUnionAlias());
        target.setUnionTag(source.getUnionTag());
        target.setUnionTrafficGroup(source.getUnionTrafficGroup());
        target.setValidCode(source.getValidCode());
        target.setSubUnionId(source.getSubUnionId());
        target.setTraceType(source.getTraceType());
        target.setPayMonth(source.getPayMonth());
        target.setPopId(source.getPopId());
        target.setExt1(source.getExt1());
        target.setCpActId(source.getCpActId());
        target.setUnionRole(source.getUnionRole());
        target.setGiftCouponKey(source.getGiftCouponKey());
        target.setGiftCouponOcsAmount(source.getGiftCouponOcsAmount());
    }

    public static void main(String[] args) {
        CodeUtils.createJavaCode(OrderGoods.class, ListOrderRes.OrderGoodsRes.class);
    }

}
