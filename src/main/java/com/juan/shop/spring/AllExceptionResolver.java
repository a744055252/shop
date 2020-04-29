package com.juan.shop.spring;

import com.juan.shop.common.Result;
import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author liguanhuan
 */
@Slf4j
public class AllExceptionResolver implements HandlerExceptionResolver {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final Charset defaultCharset;

    public AllExceptionResolver() {
        this.defaultCharset = DEFAULT_CHARSET;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ServletServerHttpResponse sshr = new ServletServerHttpResponse(response);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=utf-8");
        Charset charset = this.getContentTypeCharset(sshr.getHeaders().getContentType());
        int code;
        String msg = "未知异常";
        if (ex instanceof LogicException) {
            code = ((LogicException)ex).getCode();
            msg = ex.getMessage();
        } else if (ex instanceof MethodArgumentNotValidException) {
            BindingResult br = ((MethodArgumentNotValidException)ex).getBindingResult();

            for (ObjectError error : br.getAllErrors()) {
                log.error("===请求[" + request.getRequestURL() + "," + request.getQueryString() + "]参数非法:" + error.getDefaultMessage());
            }

            code = -249;
            if (br.getAllErrors() != null && !br.getAllErrors().isEmpty()) {
                msg = (br.getAllErrors().get(0)).getDefaultMessage();
            } else {
                msg = "参数非法";
            }
        } else {
            log.error("===请求[" + request.getRequestURL() + "," + request.getQueryString() + "]异常", ex);
            code = -255;
        }

        Result result = Result.valueOfFail(code, msg);

        try {
            StreamUtils.copy(JsonUtils.encodeJson(result), charset, sshr.getBody());
            sshr.getBody().flush();
            sshr.close();
        } catch (IOException var12) {
            log.error("===请求异常" + request.getQueryString(), var12);
        }

        return new ModelAndView();
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharset() != null ? contentType.getCharset() : this.defaultCharset;
    }
}
