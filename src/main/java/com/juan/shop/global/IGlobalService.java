package com.juan.shop.global;

/**
 * @author guanhuan_li
 */
public interface IGlobalService {

    String getValue(GlobalKey key);

    void set(GlobalKey key, String value);

}
