package com.juan.shop.material;

import com.juan.shop.common.PageRes;
import com.juan.shop.common.Result;
import com.juan.shop.material.model.*;
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
@RestController
@RequestMapping("material")
public class MaterialController {

    @Autowired
    private IMaterialSerivce materialSerivce;

    @RequestMapping("add")
    private Result<Void> add(@RequestBody @Valid AddMaterialReq req) {
        User loginUser = SpringUtils.getLoginUser();
        materialSerivce.add(req, loginUser.getId());
        return Result.valueOfOkNull();
    }

    @RequestMapping("del")
    private Result<Void> del(@RequestBody @Valid DelMaterialReq req) {
        materialSerivce.del(req.getId());
        return Result.valueOfOkNull();
    }

    @RequestMapping("edit")
    private Result<Void> edit(@RequestBody @Valid EditMaterialReq req) {
        materialSerivce.edit(req);
        return Result.valueOfOkNull();
    }

    @RequestMapping("list")
    private Result<PageRes<ListMaterialRes>> list(@RequestBody @Valid ListMaterialReq req) {
        User user = SpringUtils.getLoginUser();
        PageRes<ListMaterialRes> page = materialSerivce.list(req, user.getId());
        return Result.valueOfOk(page);
    }

    @RequestMapping("share/get")
    private Result<GetShareUrlRes> getShareUrl(@RequestBody @Valid GetShareUrlReq req) {
        User loginUser = SpringUtils.getLoginUser();
        String shapeUrl = materialSerivce.createShapeUrl(req.getMaterialId(), loginUser.getId());
        GetShareUrlRes getShareUrlRes = new GetShareUrlRes();
        getShareUrlRes.setShareUrl(shapeUrl);
        return Result.valueOfOk(getShareUrlRes);
    }
}
