package com.nagisazz.base.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求通用封装类
 */
@Slf4j
@RequiredArgsConstructor
public class RestHelper {

    private final RestTemplate restTemplate;

    private final RestTemplate baseRestTemplate;

    /**
     * 执行 get无请求体
     *
     * @param uri 路径
     * @return 返回值
     */
    public String get(String uri) {
        return excute(uri, HttpMethod.GET);
    }

    /**
     * 执行 get无请求体
     *
     * @param uri   路径
     * @param clazz 响应体类型
     * @return 返回值
     */
    public <T> T get(String uri, Class<T> clazz, Object... uriVariables) {
        return excute(uri, null, HttpMethod.GET, clazz, uriVariables);
    }

    /**
     * 执行 delete无请求体
     *
     * @param uri 路径
     * @return 返回值
     */
    public String delete(String uri) {
        return excute(uri, HttpMethod.DELETE);
    }

    /**
     * 执行 post
     *
     * @param uri  路径
     * @param body 请求体
     * @return 返回值
     */
    public <T> String post(String uri, T body) {
        return excute(uri, body, HttpMethod.POST);
    }

    /**
     * 执行 post
     *
     * @param uri  路径
     * @param body 请求体
     * @return 返回值
     */
    public <T, V> V post(String uri, T body, Class<V> res) {
        return excute(uri, body, HttpMethod.POST, res);
    }

    /**
     * 执行 put
     *
     * @param uri  路径
     * @param body 请求体
     * @return 返回值
     */
    public <T> String put(String uri, T body) {
        return excute(uri, body, HttpMethod.PUT);
    }

    /**
     * 执行 patch
     *
     * @param uri  路径
     * @param body 请求体
     * @return 返回值
     */
    public <T> String patch(String uri, T body) {
        return excute(uri, body, HttpMethod.PATCH);
    }

    /**
     * 执行 patch
     *
     * @param uri
     * @param body
     * @param clazz
     * @param <T>
     * @param <V>
     * @return
     */
    public <T, V> V patch(String uri, T body, Class<V> clazz) {
        return excute(uri, body, HttpMethod.PATCH, clazz);
    }

    /**
     * 执行 patch
     *
     * @param uri
     * @param body
     * @param clazz
     * @param <T>
     * @param <V>
     * @return
     */
    public <T, V> V patch(String uri, T body, Class<V> clazz, Object... uriVariables) {
        return excute(uri, body, HttpMethod.PATCH, clazz, uriVariables);
    }

    /**
     * 执行 无请求体
     *
     * @param uri    路径
     * @param method 请求方法
     * @return 返回值
     */
    public String excute(String uri, HttpMethod method) {
        return excute(uri, null, method);
    }

    /**
     * 执行 有请求体
     *
     * @param uri    路径
     * @param body   请求体
     * @param method 请求方法
     * @return 返回值
     */
    public <T> String excute(String uri, T body, HttpMethod method) {
        return excute(uri, body, method, String.class);
    }

    /**
     * 执行 有请求体
     *
     * @param uri    路径
     * @param body   请求体
     * @param method 请求方法
     * @return 返回值
     */
    public <T, V> V excute(String uri, T body, HttpMethod method, Class<V> res,
                           Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return excute(uri, body, method, headers, res, uriVariables);
    }

    /**
     * 执行,根据要求的返回值类型决定是否使用fastjson的转换器
     *
     * @param uri    路径
     * @param body   请求体
     * @param method 请求方法
     * @return 返回值
     */
    public <T, V> V excute(String uri, T body, HttpMethod method, HttpHeaders headers,
                           Class<V> res, Object... uriVariables) {
        return excute(uri, body, method, headers, res,
                res == String.class ? baseRestTemplate : restTemplate, uriVariables);
    }

    /**
     * 执行 有请求体
     *
     * @param uri    路径
     * @param body   请求体
     * @param method 请求方法
     * @return 返回值
     */
    public <T, V> V excuteSimple(String uri, T body, HttpMethod method, Class<V> res,
                                 Object... uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        return excute(uri, body, method, headers, res, baseRestTemplate,
                uriVariables);
    }

    /**
     * 执行 有请求体
     *
     * @param uri    路径
     * @param body   请求体
     * @param method 请求方法
     * @return 返回值
     */
    public <T, V> V excuteSimple(String uri, T body, HttpMethod method, HttpHeaders headers,
                                 Class<V> res, Object... uriVariables) {
        return excute(uri, body, method, headers, res, baseRestTemplate,
                uriVariables);
    }

    private <T, V> V excute(String uri, T body, HttpMethod method, HttpHeaders headers,
                            Class<V> res, RestTemplate trueRestTemplate, Object... uriVariables) {
        if (this.baseRestTemplate == trueRestTemplate) {
            if (!(body instanceof byte[] || body instanceof String)) {
                log.info("请求服务，接口[{}]请求方式[{}]路径参数[{}]\n请求头[{}]\n请求体[{}]", uri,
                        method, uriVariables, headers, body);
                return trueRestTemplate.exchange(uri, method, new HttpEntity<>(JSON.toJSONString(body), headers), res, uriVariables).getBody();
            }
        }
        HttpEntity<T> entity = new HttpEntity<>(body, headers);
        log.info("请求服务，接口[{}]请求方式[{}]路径参数[{}]\n请求头[{}]\n请求体[{}]", uri,
                method, uriVariables, headers, body instanceof byte[] ? "二进制" : body);
        return trueRestTemplate.exchange(uri, method, entity, res, uriVariables).getBody();
    }
}
