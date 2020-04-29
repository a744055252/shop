package com.juan.shop.user.wallet;

import com.juan.shop.order.IOrderService;
import com.juan.shop.order.goodsearning.GoodsEarning;
import com.juan.shop.pay.IWechatPayService;
import com.juan.shop.pay.WechatPayInfo;
import com.juan.shop.pay.model.CheckType;
import com.juan.shop.pay.model.PayReq;
import com.juan.shop.pay.model.PayType;
import com.juan.shop.user.wallet.model.WithdrawStatus;
import com.juan.shop.wechat.IWxUserSerivce;
import com.juan.shop.wechat.WxUser;
import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
@Service
@Slf4j
public class WalletServiceImpl implements IWalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private IWechatPayService wechatPayService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IWxUserSerivce wxUserSerivce;

    @Override
    public synchronized void withdraw(Long operId) {

        List<GoodsEarning> goodsEarningList = orderService.findWithdraw(operId);
        Optional<Wallet> walletOpt = walletRepository.findByUserId(operId);

        Wallet wallet = walletOpt.orElseThrow(() -> LogicException.valueOfUnknow("用户暂无可提现金额！"));
        if (wallet.getActual() < 1) {
            throw LogicException.valueOfUnknow("用户暂无可提现金额");
        }

        // 检查金额是否正确
        double sum = goodsEarningList.stream().mapToDouble(GoodsEarning::getActualFee).sum();
        if (!wallet.getActual().equals(sum)) {
            throw LogicException.valueOfUnknow("金额数量不正确，请联系后台管理员查看！");
        }

        Optional<WxUser> wxUserOpt = wxUserSerivce.findByUserId(operId);
        WxUser wxUser = wxUserOpt.orElseThrow(() -> LogicException.valueOfUnknow("用户未绑定微信，请先绑定微信"));

        Date now = new Date();

        wallet.setActual(0D);
        wallet.setUpdateDate(now);

        Withdraw withdraw = new Withdraw();
        withdraw.setUserId(operId);
        withdraw.setWithdrawDate(now);
        withdraw.setNumber(sum);
        withdraw.setCreateDate(now);
        withdraw.setUpdateDate(now);
        withdraw.setStatus(WithdrawStatus.tixianzhong);
        withdrawRepository.save(withdraw);

        for (GoodsEarning goodsEarning : goodsEarningList) {
            goodsEarning.setWithdrawStatus(WithdrawStatus.tixianzhong);
            goodsEarning.setWithdrawId(withdraw.getId());
            goodsEarning.setUpdateDate(now);
        }
        orderService.saveAllGoodsEarning(goodsEarningList);

        // 申请提现
        withdraw(withdraw, wxUser, goodsEarningList);
    }

    @Override
    public synchronized void withdrawById(Long withdrawId) {
        Optional<Withdraw> withdrawOpt = withdrawRepository.findById(withdrawId);
        Withdraw withdraw = withdrawOpt.orElseThrow(() -> LogicException.valueOfUnknow("该提现申请不存在！"));

        Optional<WxUser> wxUserOpt = wxUserSerivce.findByUserId(withdraw.getUserId());
        WxUser wxUser = wxUserOpt.orElseThrow(() -> LogicException.valueOfUnknow("用户未绑定微信，请先绑定微信"));

        List<GoodsEarning> goodsEarningList = orderService.findWithdraw(withdraw.getUserId());


        withdraw(withdraw, wxUser, goodsEarningList);

    }

    private void withdraw(Withdraw withdraw, WxUser wxUser, List<GoodsEarning> goodsEarningList) {
        Integer sumInt = new Double(withdraw.getNumber() * 100).intValue();
        PayReq req = new PayReq();
        req.setAmount(sumInt);
        req.setCheck_name(CheckType.FORCE_CHECK);
        req.setDesc("推手提现");
        req.setEntityId(withdraw.getId());
        req.setType(PayType.tixian);
        req.setOpenid(wxUser.getOpenId());
        WechatPayInfo pay = null;
        try {
            pay = wechatPayService.pay(req);
        } finally {
            if (pay != null) {
                Date now1 = new Date();
                switch (pay.getStatus()) {
                    case SUCCESS:
                        withdraw.setStatus(WithdrawStatus.wancheng);
                        for (GoodsEarning goodsEarning : goodsEarningList) {
                            goodsEarning.setWithdrawStatus(WithdrawStatus.wancheng);
                            goodsEarning.setUpdateDate(now1);
                        }
                        orderService.saveAllGoodsEarning(goodsEarningList);

                        break;
                    case ERROR:
                    case FAILED:
                        withdraw.setStatus(WithdrawStatus.chucuo);
                        break;
                    case PROCESSING:
                        withdraw.setStatus(WithdrawStatus.tixianzhong);
                        break;
                    default:
                        log.error("不支持的类型");
                }
                withdraw.setUpdateDate(now1);
                withdrawRepository.save(withdraw);
            }
        }
    }

    @Override
    public void addActual(GoodsEarning goodsEarning) {

        Date now = new Date();

        Long userId = goodsEarning.getUserId();
        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        Wallet wallet = walletOpt.orElseGet(() -> {
            Wallet temp = new Wallet();
            temp.setUserId(userId);
            temp.setActual(0D);
            temp.setEstimate(0D);
            temp.setCreateDate(now);
            return temp;
        });

        wallet.setUpdateDate(now);

        Double current = wallet.getActual() + goodsEarning.getActualFee();
        wallet.setActual(current);

        walletRepository.save(wallet);

        log.info("钱包新增可提现金额:{}, 当前总共可提现金额, 收益表id:{}", goodsEarning.getActualFee(), current, goodsEarning.getId());

    }
}
