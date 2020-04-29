package com.juan.shop.user.tier;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface TierRepository extends JpaRepository<Tier, Long> {

    Optional<Tier> findByUserId(Long userId);

    Optional<Tier> findByPositionId(String positionId);

}
