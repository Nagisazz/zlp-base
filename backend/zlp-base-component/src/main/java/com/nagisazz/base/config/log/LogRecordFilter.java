package com.nagisazz.base.config.log;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.CommonWebUtil;
import com.nagisazz.base.util.RequestUtil;

import lombok.RequiredArgsConstructor;

/**
 * 日志过滤器
 */
@RequiredArgsConstructor
public class LogRecordFilter implements Filter {

    private final LogStorage logStorage;

    private final ZlpProperties zlpProperties;

    private static final String TRACE_ID_NAME = "traceId";

    private static final String REQUEST_URL_NAME = "requestUrl";

    private static final Integer LOG_CLASSIFY_LOGIN = 1;

    private static final Integer LOG_CLASSIFY_OPERATE = 2;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        // 塞入logback日志需要的变量
        MDC.put(REQUEST_URL_NAME, url);
        MDC.put(TRACE_ID_NAME, UUID.randomUUID().toString().replaceAll("-", ""));

        // 不记录放开的地址
        boolean flag = true;
        final List<String> urlAnons = Arrays.stream(StringUtils.split(zlpProperties.getLog().getPermitUrl(), ","))
                .map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        for (String urlAnon : urlAnons) {
            if (StringUtils.contains(url, urlAnon)) {
                flag = false;
                break;
            }
        }
        // 提取请求body
        LogRequestWrapper requestWrapper = null;
        // 开始发送LogRecord记录
        if (flag) {
            String param = null;
            // 先判断是否有url路径参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (!CollectionUtils.isEmpty(parameterMap)) {
                param = JSON.toJSONString(parameterMap);
            } else {
                // post方法才对request进行包装且不为上传文件时进行包装
                if ("POST".equals(request.getMethod()) && !url.contains(BaseConstant.FILE_UPLOAD_URL)) {
                    requestWrapper = new LogRequestWrapper(request);
                    param = requestWrapper.getBody();
                }
            }
            // 封装并发送LogRecord实体
            LocalDateTime now = LocalDateTime.now();
            param = StringUtils.isBlank(param) ? null : param.replaceAll("\n", "").replaceAll("\r", "");
            logStorage.saveLog(LogRecord.builder()
                    .userId(CommonWebUtil.getUserId())
                    .classify(url.contains(BaseConstant.SSO_URL) ? LOG_CLASSIFY_LOGIN : LOG_CLASSIFY_OPERATE)
                    .requestSystem(zlpProperties.getSystem().getName())
                    .requestUrl(url)
                    .requestParam(param)
                    .requestMethod(request.getMethod())
                    .requestIp(RequestUtil.getIp())
                    .createTime(now)
                    .updateTime(now)
                    .build());
        }

        // 过滤链继续
        try {
            if (Objects.isNull(requestWrapper)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        } finally {
            MDC.remove(TRACE_ID_NAME);
        }
    }
}