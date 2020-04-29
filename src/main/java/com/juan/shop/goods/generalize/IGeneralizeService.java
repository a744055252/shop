package com.juan.shop.goods.generalize;

import com.juan.shop.goods.generalize.Generalize;
import com.juan.shop.three.jd.Platform;

import java.util.Map;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface IGeneralizeService {

    /**
     * 手动添加用户和对应素材的推广链接
     * @param platform 对应的平台
     * @param materialId 素材id
     * @param id2url 用户id和对应的推广链接
     */
    void addAll(Platform platform, Long materialId, Map<Long, String> id2url);

    void delAllByMaterialIdAndPlatform(Platform platform, Long materialId);

    Optional<Generalize> findByMaterialIdAndPlatformAndUserId(Long materialId, Platform platform, Long userId);
}
