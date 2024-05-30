package com.nagisazz.base.config.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.nagisazz.base.config.page.PageResult;
import com.nagisazz.base.pojo.OperationResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后端服务返回统一格式对象, 序列化成json字符串返回
 * { "status": 0, "message": “”, "data": null }
 * status: 状态码，200成功，400参数异常，500服务器错误
 * message: 错误消息
 * data: 数据
 */
@Slf4j
@ControllerAdvice
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    private static final String PACKAGE_PREFIX = "com.nagisazz";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.getContainingClass().getPackage().getName().startsWith(PACKAGE_PREFIX);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<?
            extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse response) {
        boolean autoWrapResp = true;
        boolean autoWrapPage = false;
        WrapResp wrapResp = methodParameter.getMethodAnnotation(WrapResp.class);
        if (wrapResp == null) {
            wrapResp = methodParameter.getContainingClass().getAnnotation(WrapResp.class);
        }
        if (wrapResp != null) {
            autoWrapResp = wrapResp.autoWrapResp();
            autoWrapPage = wrapResp.autoWrapPage();
        }
        // 清空分页信息
        PageHelper.clearPage();
        // 已经正确包装的不进行处理
        if (body instanceof OperationResult) {
            return body;
        }
        if (body == null) {
            if (autoWrapPage) {
                return OperationResult.buildSuccessResult(new PageResult(null));
            } else if (autoWrapResp) {
                return OperationResult.buildSuccessResult(null);
            }
            return null;
        }
        // 返回数据
        if (autoWrapPage) {
            return OperationResult.buildSuccessResult(new PageResult((List) body));
        } else if (autoWrapResp) {
            // String单独处理
            if (body instanceof String) {
                return objectMapper.writeValueAsString(OperationResult.buildSuccessResult(body));
            }
            // 默认进行包装返回
            return OperationResult.buildSuccessResult(body);
        } else {
            return body;
        }
    }
}
