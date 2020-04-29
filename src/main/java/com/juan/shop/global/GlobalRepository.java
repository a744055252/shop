package com.juan.shop.global;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface GlobalRepository extends JpaRepository<Global, Long> {

    Optional<Global> findByGlobalKey(GlobalKey key);

}
