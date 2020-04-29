package com.juan.shop.three.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JsonUtils;

import javax.annotation.PostConstruct;

/**
 * 使用前
 * @author guanhuan_li
 */
@Component
@Slf4j
public class JdManagerImpl implements JdManager {

    @Autowired
    private JdProperties jdProperties;

    private JdClient client;

    @PostConstruct
    public void init(){
        log.info("初始化jdclient, 参数：{}", JsonUtils.encodeJson(jdProperties));
        client = new DefaultJdClient(jdProperties.getServerUrl(),
                jdProperties.getAccessToken(), jdProperties.getAppKey(), jdProperties.getAppSecret());
    }

    @Override
    public String getSideId() {
        return jdProperties.getSideId();
    }

    @Override
    public JdClient getClient() {
        return client;
    }
}
    