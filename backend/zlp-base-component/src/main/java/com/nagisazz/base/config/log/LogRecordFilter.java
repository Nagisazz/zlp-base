package com.nagisazz.base.config.log;

import com.alibaba.fastjson.JSON;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.config.log.request.LogRequestWrapper;
import com.nagisazz.base.config.log.response.LogResponseWrapper;
import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.CommonWebUtil;
import com.nagisazz.base.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志过滤器
 */
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
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
        // 使用自定义包装过滤器的响应执行chain.doFilter
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        LogResponseWrapper wrapper = new LogResponseWrapper(response);
        // 过滤链继续
        String result = null;
        try {
            if (Objects.isNull(requestWrapper)) {
                filterChain.doFilter(servletRequest, wrapper);
            } else {
                filterChain.doFilter(requestWrapper, wrapper);
            }
            // 获取返回结果（仅json返回类型）并写回输出流
            result = wrapper.writeResponse(response);
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