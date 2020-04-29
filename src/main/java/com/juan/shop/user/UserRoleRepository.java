package com.juan.shop.user;

import com.juan.shop.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liguanhuan
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 通过用户id{@link User#getId()}获取对应的角色id{@link Role#getId()}列表
     * @param userId 用户id{@link User#getId()}
     * @return 用户和角色的关联表List
     */
    List<UserRole> findAllByUserId(long userId);
}
