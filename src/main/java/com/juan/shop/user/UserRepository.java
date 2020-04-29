package com.juan.shop.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author liguanhuan
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 通过用户名字{@link User#getUsername()}查找用户
     * @param name 用户名{@link User#getUsername()}
     * @return 用户Optional对象
     */
    Optional<User> findByUsername(String name);

}
