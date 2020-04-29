package com.juan.shop.material;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.juan.shop.common.PageRes;
import com.juan.shop.file.IFileService;
import com.juan.shop.file.UploadFile;
import com.juan.shop.goods.generalize.Generalize;
import com.juan.shop.goods.generalize.IGeneralizeService;
import com.juan.shop.material.model.AddMaterialReq;
import com.juan.shop.material.model.EditMaterialReq;
import com.juan.shop.material.model.ListMaterialReq;
import com.juan.shop.material.model.ListMaterialRes;
import com.juan.shop.three.jd.JdManager;
import com.juan.shop.three.jd.Platform;
import com.juan.shop.user.IUserService;
import com.juan.shop.user.User;
import exception.LogicException;
import jd.union.open.promotion.common.get.request.PromotionCodeReq;
import jd.union.open.promotion.common.get.request.UnionOpenPromotionCommonGetRequest;
import jd.union.open.promotion.common.get.response.UnionOpenPromotionCommonGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
@Slf4j
@Service
public class MaterialServiceImpl implements IMaterialSerivce {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IGeneralizeService generalizeService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private JdManager jdManager;

    @Override
    public void add(AddMaterialReq req, Long operId) {

        for (Long id : req.getId2url().keySet()) {
            Optional<User> userOpt = userService.get(id);
            userOpt.orElseThrow(()-> LogicException.valueOfUnknow("用户id:"+id+"不存在！"));
        }

        Date now = new Date();
        Material material = new Material();
        material.setContent(req.getContent());
        material.setImgFileId(req.getImgFileId());
        material.setUserId(operId);
        material.setGoodsUrl(req.getGoodsUrl());
        material.setUpdateDate(now);
        material.setCreateDate(now);
        materialRepository.save(material);

        log.info("新增素材id：{}", material.getId());

        generalizeService.addAll(Platform.jd, material.getId(), req.getId2url());

    }

    @Override
    public void del(Long id) {
        Optional<Material> materialOpt = materialRepository.findById(id);
        Material material = materialOpt.orElseThrow(() ->
                LogicException.valueOfUnknow("素材id：" + id + " 不存在！"));

        materialRepository.delete(material);

        log.info("删除素材{}", material);
    }

    @Override
    public void edit(EditMaterialReq req) {

        Optional<Material> materialOpt = materialRepository.findById(req.getId());
        Material material = materialOpt.orElseThrow(() -> LogicException.valueOfUnknow("素材id：" + req.getId() + "不存在！"));

        for (Long id : req.getId2url().keySet()) {
            Optional<User> userOpt = userService.get(id);
            userOpt.orElseThrow(()-> LogicException.valueOfUnknow("用户id:"+id+"不存在！"));
        }

        Date now = new Date();
        material.setImgFileId(req.getImgFileId());
        material.setGoodsUrl(req.getGoodsUrl());
        material.setContent(req.getContent());
        material.setUpdateDate(now);
        materialRepository.save(material);

        log.info("修改素材id:{}", material.getId());

        generalizeService.delAllByMaterialIdAndPlatform(Platform.jd, material.getId());

        generalizeService.addAll(Platform.jd, material.getId(), req.getId2url());
    }

    @Override
    public PageRes<ListMaterialRes> list(ListMaterialReq req, Long operId) {

        Page<Material> page = materialRepository.findAll(req.toPageabel());

        List<Material> content = page.getContent();

        List<ListMaterialRes> resList = new ArrayList<>();

        UploadFile defaultFile = new UploadFile();
        defaultFile.setSrc("");

        for (Material material : content) {
            Optional<Generalize> generalizeOpt = generalizeService
                    .findByMaterialIdAndPlatformAndUserId(material.getId(), Platform.jd, operId);

            ListMaterialRes res = new ListMaterialRes();
            res.setId(material.getId());
            res.setContent(material.getContent());

            if (material.getImgFileId() != null) {
                Optional<UploadFile> fileOpt = fileService.findById(material.getImgFileId());
                UploadFile file = fileOpt.orElse(defaultFile);
                res.setImg(file.getSrc());
            }

            generalizeOpt.ifPresent(generalize -> res.setUrl(generalize.getUrl()));

            Optional<User> userOpt = userService.get(material.getUserId());
            userOpt.ifPresent(user -> res.setUserName(user.getName()));

            resList.add(res);
        }

        return PageRes.valueOf(page, resList);
    }

    @Override
    public String createShapeUrl(Long materialId, Long operId) {

        String positionId = userService.getPositionId(operId);
        Optional<Material> materialOpt = materialRepository.findById(operId);
        Material material = materialOpt.orElseThrow(() -> LogicException.valueOfUnknow("素材id的数据不存在！"));

        return createShareUrl(material.getGoodsUrl(), positionId);
    }

    private String createShareUrl(String url, String positionId) {

        JdClient client = jdManager.getClient();
        String sideId = jdManager.getSideId();
        UnionOpenPromotionCommonGetRequest request=new UnionOpenPromotionCommonGetRequest();
        PromotionCodeReq promotionCodeReq=new PromotionCodeReq();

        promotionCodeReq.setMaterialId(url);
        promotionCodeReq.setSiteId(sideId);
        promotionCodeReq.setPositionId(Long.valueOf(positionId));

        request.setPromotionCodeReq(promotionCodeReq);
        UnionOpenPromotionCommonGetResponse response;
        try {
            response = client.execute(request);
        } catch (JdException e) {
            log.error("推广链接生成失败！", e);
            throw LogicException.valueOfUnknow("生成推广链接失败，请稍后再试!");
        }

        return response.getData().getClickURL();
    }
}
