package com.juan.shop.auth;

import com.juan.shop.common.OrderIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @author liguanhuan
 */
@Service
@Order(OrderIndex.AUTH)
@Slf4j
public class AuthServiceImpl implements IAuthService, ApplicationRunner {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private WebApplicationContext applicationContext;

    /** 所有权限 */
    private static Set<Auth> allAuth;

    @Override
    public Optional<Auth> get(Long authId) {
        return authRepository.findById(authId);
    }

    @Override
    public List<Auth> findAll() {
        return authRepository.findAll();
    }

    @Override
    public Set<Auth> getAll() {
        return allAuth;
    }

    @Override
    public Optional<Auth> findByUrl(String url) {
        return authRepository.findByUrl(url);
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("auth data init running !");

        List<Auth> all = findAll();
        HashSet<Auth> tempSet = new HashSet<>(all);
        allAuth = Collections.unmodifiableSet(tempSet);

        if (!all.isEmpty()) {
            return;
        }

        log.info("初始化权限");

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取 url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Auth> auths = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = map.get(info);
            PostMapping methodAnnotation = handlerMethod.getMethodAnnotation(PostMapping.class);
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns) {
                Auth temp = new Auth();
                temp.setUrl(url);
                if (methodAnnotation != null) {
                    temp.setName(methodAnnotation.name());
                }

                auths.add(temp);
            }
        }

        authRepository.saveAll(auths);
        StringBuilder sb = new StringBuilder();
        auths.forEach(auth -> sb.append(auth.toString()).append("\r\n"));

        log.info("权限列表----start");
        log.info(sb.toString());
        log.info("权限列表----end");
    }


}
