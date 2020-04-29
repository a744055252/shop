package com.juan.shop.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author liguanhuan
 */
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUrl(String url);
}
