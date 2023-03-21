package com.nagisazz.base.config.log;

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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.alibaba.fastjson.JSON;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.config.log.request.LogRequestWrapper;
import com.nagisazz.base.config.log.response.LogResponseWrapper;
import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.CommonWebUtil;
import com.nagisazz.base.util.RequestUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志过滤器
 */
@Slf4j
@RequiredArgsConstructor
public class LogRecordFilter implements Filter {

    private static final String TRACE_ID_NAME = "traceId";
    private static final String REQUEST_URL_NAME = "requestUrl";
    private static final Integer LOG_CLASSIFY_LOGIN = 1;
    private static final Integer LOG_CLASSIFY_OPERATE = 2;
    private final LogStorage logStorage;
    private final ZlpProperties zlpProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        // 塞入logback日志需要的变量
        MDC.put(REQUEST_URL_NAME, url);
        MDC.put(TRACE_ID_NAME, UUID.randomUUID().toString().replaceAll("-", ""));

        // 不记录放开的地址
        boolean flag = true;
        if (StringUtils.isNotBlank(zlpProperties.getLog().getPermitUrl())) {
            final List<String> urlAnons = Arrays.stream(StringUtils.split(zlpProperties.getLog().getPermitUrl(), ","))
                    .map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            for (String urlAnon : urlAnons) {
                if (StringUtils.contains(url, urlAnon)) {
                    flag = false;
                    break;
                }
            }
        }
        // 提取请求body
        LogRequestWrapper requestWrapper = null;
        // 开始发送LogRecord记录
        String param = null;
        if (flag) {
            // 先判断是否有url路径参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (MapUtils.isNotEmpty(parameterMap)) {
                param = JSON.toJSONString(parameterMap);
            } else {
                // post方法且不为上传文件时才对request进行包装
                if ("POST".equals(request.getMethod()) && !url.contains(BaseConstant.FILE_UPLOAD_URL)) {
                    requestWrapper = new LogRequestWrapper(request);
                    param = requestWrapper.getBody();
                }
            }
        }
        // 过滤链继续
        String result = null;
        try {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            // 判断响应类型是否为json
            boolean resWrapperFlag = "application/json".equals(response.getContentType());
            // 如果是json类型，创建一个自定义的响应包装器
            LogResponseWrapper wrapper = resWrapperFlag ? new LogResponseWrapper(response) : null;
            // 获取请求对象，如果有请求包装器则使用它，否则使用原始请求对象
            ServletRequest req = Objects.isNull(requestWrapper) ? servletRequest : requestWrapper;
            // 获取响应对象，如果是json类型则使用响应包装器，否则使用原始响应对象
            ServletResponse res = resWrapperFlag ? wrapper : response;
            // 将请求和响应对象传递给过滤器链中的下一个过滤器或资源
            filterChain.doFilter(req, res);
            // 如果是json类型，获取响应包装器中的返回结果，并写回到输出流中
            result = resWrapperFlag ? wrapper.writeResponse(response) : null;
        } catch (Exception e) {
            log.error("日志记录失败", e);
        } finally {
            if (flag) {
                // 封装并储存LogRecord实体
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
                        .responseResult(result)
                        .createTime(now)
                        .updateTime(now)
                        .build());
            }
            MDC.remove(TRACE_ID_NAME);
        }
    }
}