package com.juan.shop.proxy.http;

import com.juan.shop.common.Result;
import com.juan.shop.token.LoginUserReq;
import org.springframework.stereotype.Service;

/**
 * @author guanhuan_li
 */
@Service
@Post(url = "http://127.0.0.1:8280/shop")
public interface TestApi extends IHttp{

    @Post(url = "login")
    Result<String> test(@Param LoginUserReq req);

}
