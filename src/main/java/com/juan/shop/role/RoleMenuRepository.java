package com.juan.shop.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liguanhuan
 */
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {

    /**
     * 通过角色id获取权限id列表
     * @param roleId 角色id{@link Role#getId()}
     * @return 角色和权限的关联表List
     */
    List<RoleMenu> findAllByRoleId(long roleId);


}
