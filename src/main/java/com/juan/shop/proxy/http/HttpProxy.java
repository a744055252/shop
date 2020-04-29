package com.juan.shop.proxy.http;

import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;
import utils.JsonUtils;
import utils.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;

/**
 * @author guanhuan_li
 */
@Slf4j
public class HttpProxy implements InvocationHandler {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final int CONNECTION_TIMEOUT = 600000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 600000;

    private final Class<Post> postClass = Post.class;

    private final Class<HeaderGroup> headerGroupClass = HeaderGroup.class;

    private final Class<Param> paramClass = Param.class;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("http代理");

        Class<?> clazz = method.getDeclaringClass();
        String methodName = method.getName();
        Class<?> returnType = method.getReturnType();
        Post postAnno = clazz.getAnnotation(postClass);
        if (postAnno == null) {
            throw LogicException.valueOfUnknow("Http请求api应该加上@post注解");
        }

        // 指定了方法名
        Post methodPostAnno = method.getAnnotation(postClass);
        if (methodPostAnno != null) {
            methodName = methodPostAnno.url();
        }

        if (!methodName.startsWith("/")) {
            methodName = "/" + methodName;
        }

        String url = postAnno.url() + methodName;
        if (StringUtils.isBlank(url)) {
            throw LogicException.valueOfUnknow("@Post的url不能为空");
        }

        Object param = null;
        HeaderGroup group = null;
        Parameter[] parameters = method.getParameters();

        int paramIndex = 1;
        for (Parameter parameter : parameters) {
            Param paramAnno = parameter.getAnnotation(paramClass);

            if (paramAnno != null) {
                break;
            }
            paramIndex++;
        }
        for (Object arg : args) {
            paramIndex--;
            Class<?> argClass = arg.getClass();

            // 该请求参数是消息头
            if (headerGroupClass.getName().equals(argClass.getName())) {
                group = (HeaderGroup) arg;
            }

            // 改参数是请求体
            if (paramIndex == 0) {
                param = arg;
            }
        }

        log.info("url:{} \r\n param:{}", url, param);

        String res = httpPostJson(url, param, group);
        return JsonUtils.decodeJson(res, returnType);
    }

    private static String httpPostJson(String url, Object obj, HeaderGroup group) {

        if (group == null) {
            group = new HeaderGroup();
        }

        RequestConfig.Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        StringEntity entity = new StringEntity(JsonUtils.encodeJson(obj), Charset.forName("UTF-8"));
        entity.setContentEncoding(DEFAULT_CHARSET.name());
        entity.setContentType("application/json");
        HttpPost httpost = new HttpPost(url);

        for (Header header : group.getAllHeaders()) {
            httpost.setHeader(header);
        }

        httpost.setEntity(entity);

        String res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes);
            System.err.println(resStr);
            EntityUtils.consume(entityRes);
            res = resStr;
        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }
}
