package com.juan.shop.order;

import com.juan.shop.three.jd.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByPlatformAndPaltformId(Platform platform, Long paltformId);
}
