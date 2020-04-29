package com.juan.shop.material;

import com.juan.shop.common.PageRes;
import com.juan.shop.material.model.AddMaterialReq;
import com.juan.shop.material.model.EditMaterialReq;
import com.juan.shop.material.model.ListMaterialReq;
import com.juan.shop.material.model.ListMaterialRes;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface IMaterialSerivce {

    void add(AddMaterialReq req, Long operId);

    void del(Long id);

    void edit(EditMaterialReq req);

    PageRes<ListMaterialRes> list(ListMaterialReq req, Long operId);

    String createShapeUrl(Long materialId, Long operId);
}
