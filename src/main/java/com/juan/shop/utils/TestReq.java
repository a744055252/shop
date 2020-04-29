package com.juan.shop.utils;

import lombok.Data;

import java.util.List;

/**
 * @author guanhuan_li
 */
@Data
public class TestReq {

    private String a;
    private String b;
    private String c;
    private String d;
    private String e;

    private List<ABean> aBeans;
    private List<BBean> bBeans;

    @Data
    public static class ABean {
        private String f;
        private String g;
    }

    @Data
    public static class BBean {
        private String j;
        private String k;
        private List<ABean> baBeans;
    }

}
