package com.juan.shop.utils;

import com.juan.shop.common.Result;
import exception.LogicException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import utils.FileUtils;
import utils.JsonUtils;
import utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author liguanhuan
 */
@SuppressWarnings("ALL")
@Slf4j
public class HttpUtils {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final int CONNECTION_TIMEOUT = 600000;
    public static final int CONNECTION_REQUEST_TIMEOUT = 600000;


    public static void sendMsg(HttpServletRequest request, HttpServletResponse response, Object msg) {
        ServletServerHttpResponse sshr = new ServletServerHttpResponse(response);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=utf-8");
        Charset charset = getContentTypeCharset(sshr.getHeaders().getContentType());

        try {
            StreamUtils.copy(JsonUtils.encodeJson(msg), charset, sshr.getBody());
            sshr.getBody().flush();
            sshr.close();
        } catch (IOException var6) {
            log.error("===请求异常" + request.getQueryString(), var6);
        }

    }

    public static void sendMsg(HttpServletRequest request, HttpServletResponse response, Result<?> result) {
        ServletServerHttpResponse sshr = new ServletServerHttpResponse(response);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=utf-8");
        Charset charset = getContentTypeCharset(sshr.getHeaders().getContentType());

        try {
            StreamUtils.copy(JsonUtils.encodeJson(result), charset, sshr.getBody());
            sshr.getBody().flush();
            sshr.close();
        } catch (IOException var6) {
            log.error("===请求异常" + request.getQueryString(), var6);
        }

    }

    private static Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharset() != null ? contentType.getCharset() : DEFAULT_CHARSET;
    }

    public static void download(HttpServletRequest request, String path, HttpServletResponse response) {
        try {
            File file = new File(path);
            String filename = file.getName();
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename));
            response.addHeader("Content-Length", "" + file.length());
            String origin = request.getHeader("Origin");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, content-type, Accept");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }

    public static void downloadDoc(String path, HttpServletResponse response) {
        try {
            File file = new File(path);
            String filename = file.getName();
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.setCharacterEncoding("utf-8");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-word;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename));
            response.addHeader("Content-Length", "" + file.length());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public static String httppost(String url, Map<String, String> params) {
        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        List<NameValuePair> nameValuePairList = new ArrayList();
        Iterator param = params.entrySet().iterator();

        while(param.hasNext()) {
            Entry<String, String> entiry = (Entry)param.next();
            NameValuePair valuePair = new BasicNameValuePair(entiry.getKey(), entiry.getValue());
            nameValuePairList.add(valuePair);
        }

        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(nameValuePairList);
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("http-post请求参数构造异常", e);
        }

        HttpPost httpost = new HttpPost(url);
        httpost.setEntity(entity);

        String result;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes);
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            result = resStr;
        } catch (Exception e) {
            throw new RuntimeException("http-post请求异常", e);
        } finally {
            httpost.releaseConnection();
        }

        return result;
    }

    public static String httppost(String url, Map<String, String> params, Map<String, String> headers) {
        return httppost(url, params, headers, null);
    }

    public static String httppost(String url, Map<String, String> params, Map<String, String> headers, Map<String, String> responHeaders) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        List<NameValuePair> nameValuePairList = new ArrayList();
        Iterator param = params.entrySet().iterator();

        while(param.hasNext()) {
            Entry<String, String> entiry = (Entry)param.next();
            NameValuePair valuePair = new BasicNameValuePair(entiry.getKey(), entiry.getValue());
            nameValuePairList.add(valuePair);
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairList, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        String result;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            if (responHeaders != null) {
                Header[] var24 = response.getAllHeaders();
                int var12 = var24.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    Header head = var24[var13];
                    responHeaders.put(head.getName(), head.getValue());
                }
            }

            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes);
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            result = resStr;
        } catch (Exception var18) {
            throw new RuntimeException("http-post请求异常" + var18.getMessage(), var18);
        } finally {
            httpost.releaseConnection();
        }

        return result;
    }

    public static byte[] httppostByte(String url, Map<String, String> params, Map<String, String> headers) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        List<NameValuePair> nameValuePairList = new ArrayList();
        Iterator param = params.entrySet().iterator();

        while(param.hasNext()) {
            Entry<String, String> entiry = (Entry)param.next();
            NameValuePair valuePair = new BasicNameValuePair(entiry.getKey(), entiry.getValue());
            nameValuePairList.add(valuePair);
        }

        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(nameValuePairList);
        } catch (UnsupportedEncodingException var17) {
            throw new RuntimeException("http-post请求参数构造异常", var17);
        }

        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        byte[] res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            byte[] result = EntityUtils.toByteArray(entityRes);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = result;
        } catch (Exception var18) {
            throw new RuntimeException("http-post请求异常", var18);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }


    public static byte[] httppostByte(String url, Map<String, String> params, Map<String, String> headers, Map<String, String> responHeaders) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        List<NameValuePair> nameValuePairList = new ArrayList();
        Iterator param = params.entrySet().iterator();

        while(param.hasNext()) {
            Entry<String, String> entiry = (Entry)param.next();
            NameValuePair valuePair = new BasicNameValuePair(entiry.getKey(), entiry.getValue());
            nameValuePairList.add(valuePair);
        }

        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(nameValuePairList);
        } catch (UnsupportedEncodingException var19) {
            throw new RuntimeException("http-post请求参数构造异常", var19);
        }

        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        byte[] res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            if (responHeaders != null) {
                Header[] allHeaders = response.getAllHeaders();
                int length = allHeaders.length;

                for(int i = 0; i < length; ++i) {
                    Header head = allHeaders[i];
                    responHeaders.put(head.getName(), head.getValue());
                }
            }

            HttpEntity entityRes = response.getEntity();
            byte[] result = EntityUtils.toByteArray(entityRes);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = result;
        } catch (Exception var20) {
            throw new RuntimeException("http-post请求异常", var20);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }

    public static String httppostForm(String url, Map<String, Object> params, Map<String, String> headers) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();

        String paramsStr = getParams(params);

        StringEntity entity = new StringEntity(paramsStr, Charset.forName("utf-8"));
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/x-www-form-urlencoded");

        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        String res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes, "UTF-8");
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = resStr;
        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }

    public static String httppostForm(String url, Object obj, Map<String, String> headers) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();

        String params = getParams(obj);

        StringEntity entity = new StringEntity(params, Charset.forName("utf-8"));
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/x-www-form-urlencoded");

        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        String res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes, "UTF-8");
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = resStr;
        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }

    public static String httppostForm(String url, Object obj, Map<String, String> headers, String proxyIp, int port) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        builder.setProxy(new HttpHost(proxyIp, port));
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();

        String params = getAllParams(obj);

        StringEntity entity = new StringEntity(params, Charset.forName("utf-8"));
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/x-www-form-urlencoded");
        HttpPost httpost = new HttpPost(url);

        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        String res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes, "UTF-8");
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = resStr;
        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }

    public static String httpGet(String url, Object obj, Map<String, String> headers, String proxyIp, int port) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        builder.setProxy(new HttpHost(proxyIp, port));
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();

        String params = getParams(obj);

        HttpGet httpGet = new HttpGet(url+"?"+params);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpGet.setHeader(header.getKey(), header.getValue());
        }

        String res;
        try {
            HttpResponse response = client.execute(httpGet);
            System.out.println("status form get: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes, "UTF-8");
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = resStr;
        } catch (Exception var15) {
            throw new RuntimeException("http-get请求异常", var15);
        } finally {
            httpGet.releaseConnection();
        }
        return res;
    }

    public static byte[] httpGetByte(String url, Map<String, String> headers, String proxyIp, int port) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        builder.setProxy(new HttpHost(proxyIp, port));
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();

        HttpGet httpGet = new HttpGet(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpGet.setHeader(header.getKey(), header.getValue());
        }

        byte[] res;
        try {
            HttpResponse response = client.execute(httpGet);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            byte[] result = EntityUtils.toByteArray(entityRes);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }
            res = result;

        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpGet.releaseConnection();
        }

        return res;
    }


    /**
     * 获取状态码
     * @param url
     * @param headers
     * @param proxyIp
     * @param port
     * @return
     */
    public static int httpGetStatus(String url, Map<String, String> headers, String proxyIp, int port) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        // 禁止重定向
        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT).setRedirectsEnabled(false);
        HttpClientBuilder builder = HttpClients.custom();
        builder.setProxy(new HttpHost(proxyIp, port));
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();

        HttpGet httpGet = new HttpGet(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpGet.setHeader(header.getKey(), header.getValue());
        }

        HttpResponse response;
        String resStr;
        try {
            response = client.execute(httpGet);
            HttpEntity entityRes = response.getEntity();
            resStr = EntityUtils.toString(entityRes, "UTF-8");
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

        } catch (Exception var15) {
            throw new RuntimeException("http-get请求异常", var15);
        } finally {
            httpGet.releaseConnection();
        }
        System.err.println(resStr);
        return response.getStatusLine().getStatusCode();
    }

    public static String getParams(Map<String, Object> params) {
        return params.entrySet().stream().map(entry -> {
            String value;
            if (entry.getValue() == null) {
                value = "";
            } else {
                value = entry.getValue().toString();
            }

            return entry.getKey() + "=" + value;

        }).collect(Collectors.joining("&"));
    }

    /**
     * 将obj中的类型为String的字段转换为：key1=value1&key2=value2 的形式
     * @param obj
     * @return
     */
    public static String getParams(Object obj) {
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(obj.getClass());
        StringBuilder sb = new StringBuilder();
        for (Field field : allFieldsList) {
            String key = field.getName();
            String value = "";
            field.setAccessible(true);
            if (field.getType() == String.class) {
                try {
                    value = (String) field.get(obj);
                } catch (IllegalAccessException e) {
                    log.error("转换错误", e);
                    throw LogicException.valueOfUnknow("转换错误");
                }
            } else {
                // 一律用JSON转换
                try {
                    value = JsonUtils.encodeJson(field.get(obj));
                } catch (IllegalAccessException e) {
                    log.error("转换错误", e);
                    throw LogicException.valueOfUnknow("转换错误");
                }
            }
            sb.append(key).append("=").append(value).append("&");
        }
        String result = sb.toString();
        if (StringUtils.endsWith(result, "&")) {
            result = StringUtils.substring(result, 0, result.length()-1);
        }
        System.err.println(result);
        return result;
    }

    public static void main(String[] args) {

        Map<String, Object> params = new HashMap<>();
        params.put("vava", "123");
        params.put("sadf.das", "dads");
        params.put("sdaf", null);
        String params1 = getAllParams(params);
        System.out.println(params1);

//        TestReq req = new TestReq();
//        req.setA("a");
//        req.setB("b");
//        req.setC("c");
//        req.setD("d");
//        req.setE("e");
//
//        List<TestReq.ABean> aBeans = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            TestReq.ABean aBean = new TestReq.ABean();
//            aBean.setF("f"+i);
//            aBean.setG("g"+i);
//            aBeans.add(aBean);
//        }
//        req.setABeans(aBeans);
//
//        List<TestReq.BBean> bBeans = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            TestReq.BBean bBean = new TestReq.BBean();
//            bBean.setJ("j"+i);
//            bBean.setK("k"+i);
//
//            List<TestReq.ABean> baBeans = new ArrayList<>();
//            for (int j = 0; i < 2; i++) {
//                TestReq.ABean aBean = new TestReq.ABean();
//                aBean.setF("f"+i);
//                aBean.setG("g"+i);
//                baBeans.add(aBean);
//            }
//
//            bBean.setBaBeans(baBeans);
//            bBeans.add(bBean);
//        }
//        req.setBBeans(bBeans);
//
//        Map<String, String> allParams = getAllParams(req, "");
//        StringBuilder sb = new StringBuilder();
//        for (Entry<String, String> entry : allParams.entrySet()) {
//            sb.append(entry.getKey()+"="+entry.getValue()+"\r\n");
//        }
//
//        System.err.println(sb.toString());
    }

    public static String getAllParams(Object obj) {
        Map<String, String> allParams = getAllParams(obj, "");
        return allParams.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    public static Map<String, String> getAllParams(Object obj, String parent) {
        Class<Collection> collectionClass = Collection.class;
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(obj.getClass());
        Map<String, String> result = new HashMap<>();
        for (Field field : allFieldsList) {
            String key = field.getName();
            String value = "";
            field.setAccessible(true);
            if (field.getType() == String.class) {
                try {
                    value = (String) field.get(obj);
                    if (value == null) {
                        value = "";
                    }
                } catch (IllegalAccessException e) {
                    log.error("转换错误", e);
                    throw LogicException.valueOfUnknow("转换错误");
                }
                result.put(parent + key, value);
            } else if (collectionClass.isAssignableFrom(field.getType())) {
                // 集合类型对象
                Collection coll;
                try {
                    coll = (Collection) field.get(obj);
                } catch (IllegalAccessException e) {
                    log.error("转换错误", e);
                    throw LogicException.valueOfUnknow("转换错误");
                }

                Iterator iterator = coll.iterator();

                int i = 0;
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    if (ClassUtils.isPrimitiveOrWrapper(obj.getClass()) || String.class.getName().equals(obj.getClass().getName())) {
                        throw new RuntimeException("List里面存储的对象不能是基础对象或者String");
                    }

                    Map<String, String> allParams = getAllParams(next, parent + key + "[" + i + "].");
                    result.putAll(allParams);
                    i ++;
                }
            } else {
                Object fieldValue;
                try {
                    fieldValue = field.get(obj);
                } catch (IllegalAccessException e) {
                    log.error("转换错误", e);
                    throw LogicException.valueOfUnknow("转换错误");
                }
                Map<String, String> allParams = getAllParams(fieldValue, parent + key + ".");
                result.putAll(allParams);
            }
        }

        return result;
    }

    public static String httppostJson(String url, Object obj, Map<String, String> headers) {

        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        StringEntity entity = new StringEntity(JsonUtils.encodeJson(obj), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        String res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            String resStr = EntityUtils.toString(entityRes);
            System.err.println(resStr);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = resStr;
        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }

    public static byte[] httppostByteJson(String url, Object obj, Map<String, String> headers) {
        if (headers == null) {
            headers = Collections.EMPTY_MAP;
        }

        Builder config = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT);
        HttpClientBuilder builder = HttpClients.custom();
        HttpClient client = builder.setDefaultRequestConfig(config.build()).build();
        StringEntity entity = new StringEntity(JsonUtils.encodeJson(obj), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        HttpPost httpost = new HttpPost(url);
        Iterator headerIter = headers.entrySet().iterator();

        while(headerIter.hasNext()) {
            Entry<String, String> header = (Entry)headerIter.next();
            httpost.setHeader(header.getKey(), header.getValue());
        }

        httpost.setEntity(entity);

        byte[] res;
        try {
            HttpResponse response = client.execute(httpost);
            System.out.println("status form post: " + response.getStatusLine());
            HttpEntity entityRes = response.getEntity();
            byte[] result = EntityUtils.toByteArray(entityRes);
            if (entityRes != null) {
                EntityUtils.consume(entityRes);
            }

            res = result;
        } catch (Exception var15) {
            throw new RuntimeException("http-post请求异常", var15);
        } finally {
            httpost.releaseConnection();
        }

        return res;
    }

    public static void readUploadFile(HttpServletRequest request, IFileHandler fileHandler) {
        long startTime = System.currentTimeMillis();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        multipartResolver.setMaxUploadSizePerFile(104857600L);
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
            Iterator iter = multiRequest.getFileNames();

            while(iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(((String)iter.next()).toString());
                if (file != null) {
                    String ctxPath = FileUtils.getFilePath(request, "file");
                    String originalFilename = file.getOriginalFilename();
                    String newFileName = FileUtils.getNewFileName(originalFilename);
                    String path = ctxPath + File.separator + newFileName;
                    log.info("上传文件" + originalFilename + "到Path:" + path);
                    File target = null;

                    try {
                        target = new File(path);
                        if (!target.exists()) {
                            target.mkdirs();
                        }

                        file.transferTo(target);
                    } catch (IllegalStateException var15) {
                        var15.printStackTrace();
                    } catch (IOException var16) {
                        var16.printStackTrace();
                    }

                    if (target != null) {
                        try {
                            fileHandler.doFile(target);
                        } catch (Exception var14) {
                            log.info("文件解析异常", var14);
                            throw new RuntimeException(var14);
                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        log.error("上传文件的运行时间：" + String.valueOf(endTime - startTime) + "ms");
    }
}
