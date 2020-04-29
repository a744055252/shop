package com.juan.shop.role;

import java.util.List;
import java.util.Optional;

/**
 * @author liguanhuan
 */
public interface IRoleService {

    String NORMAL_ROLE = "普通用户";

    String ADMIN_ROLE = "管理员";

    String OTHER_ROLE = "游客";

    /**
     * 通过角色id获取角色对应的权限id列表
     * @param roleId 角色id{@link Role#getId()}
     * @return 角色权限的关联表List
     */
    List<RoleMenu> listRoleMenuByRoleId(long roleId);

    /**
     * 通过角色名获取角色对象
     * @param name 角色名{@link Role#getName()}
     * @return 角色对象的Optional对象
     */
    Optional<Role> getByName(String name);

    Optional<Role> get(long id);

    List<Role> findByUrl(String url);
}
