package com.juan.shop.common;


import com.juan.shop.token.ResultCode;
import lombok.Data;

/**
 * 接口返回对象
 * @author liguanhuan
 * @param <T>
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private static final Result<Void> OK = valueOfOk(null);

    public Result() {
    }

    public static <T> Result<T> valueOf(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static <T> Result<T> valueOfFail(ResultCode resultCode) {
        return valueOf(resultCode.getCode(), resultCode.getDesc(), null);
    }

    public static <T> Result<T> valueOfFail(int code, String msg) {
        return valueOf(code, msg, null);
    }

    public static <T> Result<T> valueOfOk(T data) {
        return valueOf(0, "", data);
    }

    public static <T> Result<T> valueOfOkNull() {
        return (Result<T>) OK;
    }


}