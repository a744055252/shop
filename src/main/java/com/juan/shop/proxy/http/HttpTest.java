package com.juan.shop.proxy.http;

import com.juan.shop.common.Result;
import com.juan.shop.token.LoginUserReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author guanhuan_li
 */
@Service
@Slf4j
public class HttpTest {

    @Autowired
    private TestApi testApi;

    @Scheduled(cron = "30 46 16 * * ?")
    public void init() {
        LoginUserReq req = new LoginUserReq();
        req.setUsername("admin");
        req.setPassword("123456");
        Result<String> login = testApi.test(req);
        log.info("result:{}", login);
    }

}
