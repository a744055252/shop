package com.juan.shop.user.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);

}
