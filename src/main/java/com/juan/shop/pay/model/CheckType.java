package com.juan.shop.pay.model;

/**
 * @author guanhuan_li
 */
public enum CheckType {

    /** 检查的种类 */
    FORCE_CHECK("强校验真实姓名"),
    NO_CHECK("不校验真实姓名"),
    
    ;

    private String zhName;

    CheckType(String zhName){
        this.zhName = zhName;
    }

    public String getZhName() {
        return this.zhName;
    }


}
