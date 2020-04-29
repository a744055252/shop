package com.juan.shop.global;

import lombok.Getter;

/**
 * @author guanhuan_li
 */
@Getter
public enum GlobalKey {

    /** 订单任务检查开始时间 格式yyyy-MM-dd HH:mm:ss */
    orderTaskBeginDate("订单任务检查开始时间"),
    /** 返佣的分成比例 */
    commissionRate("返佣的分成比例"),
    ;

    
    private String zhName;
        
    GlobalKey(String zhName){
        this.zhName = zhName;
    }
}
