package com.juan.shop.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author liguanhuan
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 通过角色名{@link Role#getName()}获取角色对象
     * @param name 角色名{@link Role#getName()}
     * @return 角色的optional对象
     */
    Optional<Role> getByName(String name);

    @Query(value = "SELECT r.* " +
            "FROM Auth a " +
            "INNER JOIN MenuAuth ma on ma.authId = a.id " +
            "INNER JOIN RoleMenu rm on rm.menuId = ma.menuId " +
            "INNER JOIN Role r on r.id = rm.roleId " +
            "WHERE a.url = :url " +
            "GROUP BY r.id",
            nativeQuery = true)
    List<Role> findByUrl(@Param("url") String url);
}
