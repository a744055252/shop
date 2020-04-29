package com.juan.shop.user;

import com.juan.shop.common.Result;
import com.juan.shop.user.model.InviteReq;
import com.juan.shop.user.model.UserInfoRes;
import com.juan.shop.user.model.WxLoginReq;
import com.juan.shop.user.model.WxLoginRes;
import com.juan.shop.user.wallet.IWalletService;
import com.juan.shop.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author guanhuan_li
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IWalletService walletService;

    @RequestMapping("info")
    public Result<UserInfoRes> info() {
        long id = SpringUtils.getLoginUser().getId();

        UserInfoRes res = userService.info(id);
        return Result.valueOfOk(res);
    }

    @RequestMapping("wxLogin")
    public Result<WxLoginRes> wxLogin(@RequestBody @Valid WxLoginReq req){
        WxLoginRes res = userService.wechatLogin(req);
        return Result.valueOfOk(res);
    }

    @RequestMapping("invite")
    public Result<Void> invite(@RequestBody @Valid InviteReq req) {
        User loginUser = SpringUtils.getLoginUser();
        userService.invite(loginUser.getId(), req.getInvite());
        return Result.valueOfOkNull();
    }

    @RequestMapping("invite/get")
    public Result<String> createInvite() {
        User loginUser = SpringUtils.getLoginUser();
        String invite = userService.createInvite(loginUser.getId());
        return Result.valueOfOk(invite);
    }

    @RequestMapping("withdraw")
    public Result<Void> withdraw() {
        User loginUser = SpringUtils.getLoginUser();
        walletService.withdraw(loginUser.getId());
        return Result.valueOfOkNull();
    }

}
