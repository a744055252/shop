package com.juan.shop.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author guanhuan_li
 */
@Service
@Slf4j
public class ProxyTest {

    @Autowired
    private MyTestJpa myTestJpa;

    @PostConstruct
    public void init() {
        log.info("init");
        MyEntity test = myTestJpa.myTest("test");
        log.info("result:{}", test);
    }

}
