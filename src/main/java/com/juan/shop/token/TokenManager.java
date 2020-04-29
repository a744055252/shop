package com.juan.shop.token;

/**
 * @author liguanhuan
 */
public interface TokenManager {

    String createToken(String username);

    String getUserFromToken(String token);

    void removeToken(String token);

}