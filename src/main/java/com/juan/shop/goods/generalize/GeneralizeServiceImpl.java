package com.juan.shop.goods.generalize;

import com.juan.shop.three.jd.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author guanhuan_li
 */
@Slf4j
@Service
public class GeneralizeServiceImpl implements IGeneralizeService {

    @Autowired
    private GeneralizeRepository generalizeRepository;

    @Override
    public void addAll(Platform platform, Long materialId, Map<Long, String> id2url) {

        Date now = new Date();

        List<Generalize> generalizes = new ArrayList<>(id2url.size());

        for (Map.Entry<Long, String> idAndUrl : id2url.entrySet()) {
            Generalize generalize = new Generalize();
            generalize.setMaterialId(materialId);
            generalize.setPlatform(platform);
            generalize.setMaterialId(materialId);
            generalize.setUserId(idAndUrl.getKey());
            generalize.setUrl(idAndUrl.getValue());
            generalize.setUpdateDate(now);
            generalize.setCreateDate(now);
            generalizes.add(generalize);
        }

        generalizeRepository.saveAll(generalizes);

        log.info("新增平台:" + platform.getZhName() + "，素材:" + materialId + "," + generalizes.size() + "条推广链接");
    }

    @Override
    public void delAllByMaterialIdAndPlatform(Platform platform, Long materialId) {
        generalizeRepository.deleteAllByMaterialIdAndPlatform(materialId, platform);

        log.info("删除平台{}, 素材{}的所有推广链接！", platform.getZhName(), materialId);

    }

    @Override
    public Optional<Generalize> findByMaterialIdAndPlatformAndUserId(Long materialId, Platform platform, Long userId) {
        return generalizeRepository.findByMaterialIdAndPlatformAndUserId(materialId, platform, userId);
    }

}
