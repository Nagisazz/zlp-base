package com.nagisazz.base.config.rest;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * rest error处理
 */
@Slf4j
public class RestErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        if (statusCode != null) {
            if (statusCode.is3xxRedirection()) {
                log.error("状态码为{}，服务端返回重定向了，可能是被登陆拦截", rawStatusCode);
            } else if (403 == statusCode.value()) {
                log.error("状态码为{}，服务器返回客户端错误，授权不通过", rawStatusCode);
            } else if (statusCode.is4xxClientError()) {
                log.error("状态码为{}，服务器返回客户端错误，可能参数异常或资源不存在", rawStatusCode);
            } else if (statusCode.is5xxServerError()) {
                log.error("状态码为{}，服务器返回服务端错误，可能参数异常或资源不存在", rawStatusCode);
            }
        } else {
            HttpStatus.Series series = HttpStatus.Series.resolve(rawStatusCode);
            if (series == HttpStatus.Series.REDIRECTION) {
                log.error("状态码为{}，服务端返回重定向了，可能是被登陆拦截", rawStatusCode);
            } else if (series == HttpStatus.Series.CLIENT_ERROR) {
                log.error("状态码为{}，服务器返回客户端错误，可能参数异常或资源不存在", rawStatusCode);
            } else if (series == HttpStatus.Series.SERVER_ERROR) {
                log.error("状态码为{}，服务器返回服务端错误，可能服务端抛出异常", rawStatusCode);
            }
        }
        return false;
    }

    /**
     * 暂无需异常处理场景
     *
     * @param response
     * @throws IOException
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new UnsupportedOperationException("暂无需异常处理场景");
    }
}
