package com.juan.shop.user.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findByInviteCode(String inviteCode);

    Optional<Invite> findByUserId(Long userId);

}
