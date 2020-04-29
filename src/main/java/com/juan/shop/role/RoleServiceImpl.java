package com.juan.shop.role;

import com.juan.shop.auth.Auth;
import com.juan.shop.auth.AuthServiceImpl;
import com.juan.shop.common.OrderIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liguanhuan
 */
@Service
@Slf4j
@Order(OrderIndex.ROLE)
public class RoleServiceImpl implements IRoleService, ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Override
    public void run(ApplicationArguments args) {

        log.info("role data init running !");

        Date now = new Date();

        // 初始化2个角色
        Optional<Role> adminOpt = roleRepository.getByName(ADMIN_ROLE);
        if (!adminOpt.isPresent()) {
            // 初始化一个管理员
            Role role = new Role();
            role.setName(ADMIN_ROLE);
            role.setAuth(ADMIN_ROLE);
            role.setCreateDate(now);
            role.setUpdateDate(now);
            role.setRemark("系统初始化创建");

            roleRepository.save(role);

            log.info("添加" + ADMIN_ROLE + "角色！");

            // 添加权限
            List<Auth> allAuths = authService.findAll();
            List<Long> authIds = allAuths.stream().map(Auth::getId).collect(Collectors.toList());

            addAll(role.getId(), authIds);
        }

        Optional<Role> normalOpt = roleRepository.getByName(NORMAL_ROLE);
        if (!normalOpt.isPresent()) {
            // 初始化一个普通用户
            Role role = new Role();
            role.setName(NORMAL_ROLE);
            role.setAuth(NORMAL_ROLE);
            role.setCreateDate(now);
            role.setUpdateDate(now);
            role.setRemark("系统初始化创建");

            roleRepository.save(role);

            log.info("添加" + NORMAL_ROLE + "角色！");
        }
    }


    private List<RoleMenu> addAll(long roleId, List<Long> menuIds){
        Date now = new Date();
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long authId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(authId);
            roleMenu.setRoleId(roleId);
            roleMenu.setCreateDate(now);
            roleMenu.setUpdateDate(now);
            roleMenus.add(roleMenu);
        }
        roleMenuRepository.saveAll(roleMenus);

        log.info("角色[{}],新增菜单{}", roleId, menuIds.toString());

        return roleMenus;
    }

    @Override
    public List<RoleMenu> listRoleMenuByRoleId(long roleId) {
        return roleMenuRepository.findAllByRoleId(roleId);
    }

    @Override
    public Optional<Role> getByName(String name) {
        return roleRepository.getByName(name);
    }

    @Override
    public Optional<Role> get(long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findByUrl(String url) {
        return roleRepository.findByUrl(url);
    }
}
