package com.juan.shop.goods.generalize;

import com.juan.shop.three.jd.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface GeneralizeRepository extends JpaRepository<Generalize, Long> {

    void deleteAllByMaterialIdAndPlatform(Long materialId, Platform platform);

    Optional<Generalize> findByMaterialIdAndPlatformAndUserId(Long materialId, Platform platform, Long userId);

}
