package com.juan.shop.token;

public enum ResultCode {

    /**
     * 通用
     */
    CODE_00000(0, "操作成功"),
    CODE_00001(1, "请求失败"),
    CODE_00002(2, "错误的请求方法"),
    CODE_00003(3, "非法的参数字段"),
    CODE_00004(4, "异常抛出"),
    CODE_00005(5, "权限不足"),
    CODE_00006(6, "分页limit参数错误"),
    CODE_00007(7, "分页offset参数错误"),
    CODE_00008(8, "请求过于频繁"),
    CODE_0009(9, "数据已存在"),
    CODE_00010(10, "数据不存在"),
    CODE_00011(11, "参数缺失"),
    CODE_00012(12, "系统维护中"),
    CODE_00013(13, "token缺失"),
    CODE_00014(14, "token失效"),
    CODE_00015(15, "签名错误"),
    CODE_00016(16, "登录失败"),

    CODE_10000(10001, "操作部分成功"),
    /**
     * 系统
     */
    CODE_30000(30001, "系统ID错误"),

    /**
     * 授权
     */
    CODE_40001(40001, "用户未找到"),
    CODE_40002(40002, "该用户状态异常"),
    CODE_40003(40003, "该用户已被删除"),
    CODE_40004(40004, "授权异常"),

    CODE_99999(99999, "签名无效");

    private Integer code;
    private String desc;

    ResultCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code匹配枚举
     * 
     * @param code
     * @return
     */
    public static ResultCode getResultCodeByCode(String code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (code.equals(resultCode.getCode())) {
                return resultCode;
            }
        }
        return null;
    }

    public static ResultCode getResultCodeByDesc(String desc) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (desc.equals(resultCode.getDesc())) {
                return resultCode;
            }
        }
        return null;
    }
}