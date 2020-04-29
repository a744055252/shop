package com.juan.shop.proxy;

/**
 * @author guanhuan_li
 */
public interface IJpa<T extends IEntity> {
    T test(String arg);
}
