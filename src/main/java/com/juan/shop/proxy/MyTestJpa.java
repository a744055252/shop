package com.juan.shop.proxy;

import org.springframework.stereotype.Service;

/**
 * @author guanhuan_li
 */
@Service
public interface MyTestJpa extends IJpa<MyEntity> {

    MyEntity myTest(String arg);

}
