package com.juan.shop.utils;

import com.juan.shop.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import utils.JsonUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author guanhuan_li
 */
@Slf4j
public abstract class ResponseUtils {

    public static void response(HttpServletResponse response, Result res) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.OK.value());
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error("获取输出流出错！", e);
        }
        try {
            out.write(JsonUtils.encodeJson(res));
        } finally {
            out.flush();
            out.close();
        }
    }
}
