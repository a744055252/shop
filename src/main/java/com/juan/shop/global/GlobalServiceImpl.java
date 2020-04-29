package com.juan.shop.global;

import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author guanhuan_li
 */
@Slf4j
@Service
public class GlobalServiceImpl implements IGlobalService {

    @Autowired
    private GlobalRepository globalRepository;

    @PostConstruct
    public void init() {

        List<Global> all = globalRepository.findAll();
        Set<String> keySet = all.stream().map(global -> global.getGlobalKey()
                .name()).collect(Collectors.toSet());

        List<Global> globals = new ArrayList<>(2);
        Date now = new Date();
        // 初始化配置
        {

            GlobalKey key = GlobalKey.commissionRate;
            String value = "0.8";
            if (!keySet.contains(key.name())) {
                Global global = new Global();
                global.setUpdateDate(now);
                global.setCreateDate(now);
                global.setGlobalKey(key);
                global.setGlobalValue(value);
                globals.add(global);
            }

        }
        {
            GlobalKey key = GlobalKey.orderTaskBeginDate;
            String value = "2020-01-19 17:26:00";
            if (!keySet.contains(key.name())) {
                Global global = new Global();
                global.setUpdateDate(now);
                global.setCreateDate(now);
                global.setGlobalKey(key);
                global.setGlobalValue(value);
                globals.add(global);
            }
        }

        String globalsStr = globals.stream().map(global -> global.getGlobalKey().name() + ":" +
                global.getGlobalValue()).collect(Collectors.joining("\r\n"));

        log.info("配置初始化：\r\n{}", globalsStr);

        globalRepository.saveAll(globals);

    }

    @Override
    public String getValue(GlobalKey key) {

        Optional<Global> globalOpt = globalRepository.findByGlobalKey(key);
        globalOpt.orElseThrow(() -> LogicException.valueOfUnknow("系统配置：" + key.getZhName() + "没有配置值!"));

        return globalOpt.get().getGlobalValue();
    }

    @Override
    public void set(GlobalKey key, String value) {
        Optional<Global> globalOpt = globalRepository.findByGlobalKey(key);
        Date now = new Date();
        Global global = globalOpt.orElseGet(() -> {
            Global temp = new Global();
            temp.setGlobalKey(key);
            temp.setCreateDate(now);
            return temp;
        });

        global.setGlobalValue(value);
        global.setUpdateDate(now);

        globalRepository.save(global);
        
        log.info("更新系统配置：{}:{}", global.getGlobalKey(), global.getGlobalValue());

    }
}
