package com.juan.shop.user;

import com.juan.shop.common.OrderIndex;
import com.juan.shop.menu.IMenuService;
import com.juan.shop.menu.Menu;
import com.juan.shop.role.IRoleService;
import com.juan.shop.role.Role;
import com.juan.shop.role.RoleMenu;
import com.juan.shop.role.RoleServiceImpl;
import com.juan.shop.token.TokenManager;
import com.juan.shop.user.invite.Invite;
import com.juan.shop.user.invite.InviteRepository;
import com.juan.shop.user.model.*;
import com.juan.shop.user.tier.Tier;
import com.juan.shop.user.tier.TierRepository;
import com.juan.shop.utils.InviteUtils;
import com.juan.shop.wechat.IWechatService;
import com.juan.shop.wechat.IWxUserSerivce;
import com.juan.shop.wechat.WxUser;
import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import util.JsonUtils;
import weixin.popular.bean.sns.Jscode2sessionResult;
import weixin.popular.bean.wxa.WxaDUserInfo;
import weixin.popular.util.WxaUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liguanhuan
 */
@Service
@Slf4j
@Order(OrderIndex.USER)
public class UserServiceImpl implements UserDetailsService, ApplicationRunner, IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IWechatService wechatService;

    @Autowired
    private IWxUserSerivce wxUserSerivce;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private TierRepository tierRepository;

    private static final String ADMIN = "admin";

    @Override
    public String createInvite(Long operId) {
        Optional<Invite> inviteOpt = inviteRepository.findByUserId(operId);

        if (inviteOpt.isPresent()) {
            return inviteOpt.get().getInviteCode();
        }

        Optional<Tier> tierOpt = tierRepository.findByUserId(operId);
        Tier tier = tierOpt.orElseThrow(() -> LogicException.valueOfUnknow("用户未填写邀请码！"));

        String inviteCode = InviteUtils.gen(operId);
        Date now = new Date();

        Invite invite = new Invite();
        invite.setDaoshiId(tier.getDaoshiId());
        invite.setInviteCode(inviteCode);
        invite.setUserId(operId);
        invite.setShangjiId(operId);
        invite.setCreateDate(now);
        invite.setUpdateDate(now);

        inviteRepository.save(invite);

        log.info("创建用户{}邀请码{}", operId, inviteCode);

        return invite.getInviteCode();
    }

    @Override
    public WxLoginRes wechatLogin(WxLoginReq req) {

        Jscode2sessionResult result = wechatService.miniprogramLogin(req.getCode());

        WxaDUserInfo wxaDUserInfo = WxaUtil.decryptUserInfo(result.getSession_key(), req.getEncryptedData(), req.getIv());

        Optional<WxUser> wxUserOpt = wxUserSerivce.findByOpenId(wxaDUserInfo.getOpenId());

        Date now = new Date();
        WxUser wxUser = wxUserOpt.orElseGet(() -> {
            // 注册用户
            User user = new User();
            user.setUsername("wechat_" + wxaDUserInfo.getOpenId() + "_" + System.currentTimeMillis());
            user.setPassword("123456");
            user.setGender(Gender.valueOfWx(wxaDUserInfo.getGender()));
            user.setName(wxaDUserInfo.getNickName());
            user.setCreateDate(now);
            user.setUpdateDate(now);
            add(user);

            WxUser temp = new WxUser();
            convert(wxaDUserInfo, temp);
            temp.setUserId(user.getId());
            wxUserSerivce.save(temp);

            log.info("新增微信用户：{}, openId:{}", user.getId(), wxaDUserInfo.getOpenId());

            return temp;
        });

        Long userId = wxUser.getUserId();
        Optional<User> userOpt = userRepository.findById(userId);
        User user = userOpt.orElseThrow(() -> LogicException.valueOfUnknow("用户" + userId + "不存在！"));
        String token = tokenManager.createToken(user.getUsername());


        Optional<Tier> tierOpt = tierRepository.findByUserId(userId);
        WxLoginRes res = new WxLoginRes();
        res.setToken(token);
        res.setFlag(tierOpt.isPresent());

        return res;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByUsername(s);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("用户不存在！"));

        fillUser(user);

        return user;
    }

    @Override
    public UserInfoRes info(long id) {
        Optional<User> userOpt = get(id);
        User user = userOpt.orElseThrow(()->LogicException.valueOfUnknow("id:{" + id + "}的用户数据不存在！"));

        fillUser(user);

        UserInfoRes res = new UserInfoRes();
        convert(user, res);

        List<RoleMenu> roleMenus = roleService.listRoleMenuByRoleId(id);
        List<Menu> menus = roleMenus.stream()
                .map(roleMenu -> menuService.get(roleMenu.getMenuId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        res.setMenus(menus);


        return res;
    }

    @Override
    public void invite(Long operId, String inviteCode) {

        Optional<Tier> tierOpt = tierRepository.findByUserId(operId);
        if (tierOpt.isPresent()) {
            throw LogicException.valueOfUnknow("用户已关联邀请码！");
        }

        Optional<Invite> inviteOpt = inviteRepository.findByInviteCode(inviteCode);
        Invite invite = inviteOpt.orElseThrow(() -> LogicException.valueOfUnknow("邀请码：" + inviteCode + "错误!"));

        Date now = new Date();
        Tier tier = new Tier();
        tier.setUserId(operId);
        tier.setDaoshiId(invite.getDaoshiId());
        tier.setShangjiId(invite.getShangjiId());
        tier.setUpdateDate(now);
        tier.setCreateDate(now);

        tierRepository.save(tier);

        log.info("用户{}，关联层级{}", operId, JsonUtils.encodeJson(tier));

    }

    @Override
    public Optional<Tier> getTier(Long userId) {
        return tierRepository.findByUserId(userId);
    }

    @Override
    public String getPositionId(Long userId) {
        Optional<Tier> tierOpt = tierRepository.findByUserId(userId);
        Tier tier = tierOpt.orElseThrow(() -> LogicException.valueOfUnknow("用户无推广位id"));
        return tier.getPositionId();
    }

    @Override
    public User findByPositionId(String positionId) {
        Optional<Tier> tierOpt = tierRepository.findByPositionId(positionId);
        Tier tier = tierOpt.orElseThrow(() ->
                LogicException.valueOfUnknow("positionId:" + positionId + " 不存在！"));
        Optional<User> userOpt = userRepository.findById(tier.getUserId());

        return userOpt.orElseThrow(() ->
                LogicException.valueOfUnknow("层级表数据有误请检查：" + JsonUtils.encodeJson(tier)));
    }

    private void fillUser(User user) {
        if (isAdmin(user)) {
            Optional<Role> roleOpt = roleService.getByName(IRoleService.ADMIN_ROLE);
            Role admin = roleOpt.orElseThrow(() -> new IllegalArgumentException("没有管理员角色"));
            user.setRoles(Collections.singletonList(admin));
        } else {

            List<UserRole> userRoles = userRoleRepository.findAllByUserId(user.getId());

            List<Role> roles = userRoles.stream()
                    .map(userRole -> roleService.get(userRole.getRoleId()))
                    // 获取Role
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            // 用户权限
            user.setRoles(roles);
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("user data init running !");

        // 初始化一个管理员
        Optional<User> userOpt = userRepository.findByUsername(ADMIN);
        if (!userOpt.isPresent()) {
            User user = new User();
            user.setUsername(ADMIN);
            user.setName("管理员");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setGender(Gender.man);
            user.setStatus(Status.qiyong);
            user.setType(Type.admin);
            userRepository.save(user);

            log.info("新增管理员用户：" + user.toString());

            // 添加角色
            Optional<Role> roleOpt = roleService.getByName(RoleServiceImpl.ADMIN_ROLE);
            Role role = roleOpt.orElseThrow(() -> new IllegalArgumentException("没有管理员角色"));

            add(user.getId(), role.getId());
        }

    }

    private boolean isAdmin(User user) {
        return user.getType() == Type.admin;
    }

    private void add(long userId, long roleId) {
        Date now = new Date();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        userRole.setUpdateDate(now);
        userRole.setCreateDate(now);

        userRoleRepository.save(userRole);

        log.info("用户{}新增角色{}", userId, roleId);

    }

    @Override
    public synchronized void add(User user) {

        Optional<User> userOpt = userRepository.findByUsername(user.getUsername());
        if (userOpt.isPresent()) {
            throw LogicException.valueOfUnknow("该用户名" + user.getUsername() + "已存在！");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.qiyong);
        user.setType(Type.normal);
        userRepository.save(user);
        user.setPassword("***");
        log.info("新增用户{}", user);
    }

    @Override
    public Optional<User> get(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(user -> user.setPassword("***"));
        return userOpt;
    }

//    public static void main(String[] args) {
//        String inviteCode = InviteUtils.gen(operId);
//
////        CodeUtils.createJavaCode(WxaDUserInfo.class, WxUser.class);
//    }

    private static void convert(User source, UserInfoRes target) {
        target.setId(source.getId());
        target.setUsername(source.getUsername());
        target.setPassword(source.getPassword());
        target.setName(source.getName());
        target.setGender(source.getGender());
        target.setStatus(source.getStatus());
        target.setType(source.getType());
        target.setRoles(source.getRoles());
    }

    private static void convert(WxaDUserInfo source, WxUser target) {
        target.setOpenId(source.getOpenId());
        target.setUnionId(source.getUnionId());
        target.setNickName(source.getNickName());
        target.setAvatarUrl(source.getAvatarUrl());
        target.setGender(source.getGender());
        target.setCity(source.getCity());
        target.setProvince(source.getProvince());
        target.setCountry(source.getCountry());
        target.setLanguage(source.getLanguage());
    }
}
