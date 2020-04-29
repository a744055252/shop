package com.juan.shop.auth;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author liguanhuan
 */
public interface IAuthService {
    /**
     * 通过权限id获取权限对象
     * @param authId 权限id{@link Auth#getId()}
     * @return 权限的optional对象
     */
    Optional<Auth> get(Long authId);

    /**
     * 获取所有权限对象
     * @return 权限对象List
     */
    List<Auth> findAll();

    Set<Auth> getAll();

    Optional<Auth> findByUrl(String url);
}
