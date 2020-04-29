package com.juan.shop.order;

import com.juan.shop.common.PageRes;
import com.juan.shop.common.Result;
import com.juan.shop.order.model.ListOrderReq;
import com.juan.shop.order.model.ListOrderRes;
import com.juan.shop.user.User;
import com.juan.shop.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author guanhuan_li
 */
@RequestMapping("order")
@RestController
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("list")
    public Result<PageRes<ListOrderRes>> list(@RequestBody @Valid ListOrderReq req) {
        User loginUser = SpringUtils.getLoginUser();
        PageRes<ListOrderRes> page = orderService.page(req, loginUser.getId());
        return Result.valueOfOk(page);
    }

    @RequestMapping("task")
    public Result<Void> task() {
        orderService.checker();
        return Result.valueOfOkNull();
    }

}
