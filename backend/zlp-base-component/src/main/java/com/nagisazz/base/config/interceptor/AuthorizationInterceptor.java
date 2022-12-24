package com.nagisazz.base.config.interceptor;

import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.config.exception.CustomException;
import com.nagisazz.base.enums.ResultEnum;
import com.nagisazz.base.property.SystemProperties;
import com.nagisazz.base.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * token验证
 **/
@Slf4j
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final SystemProperties systemProperties;

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                             Object object) {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        // 不校验放开的地址
        final List<String> urlAnons = Arrays.stream(StringUtils.split(systemProperties.getLogin().getPermitUrl(), ","))
                .map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        for (String urlAnon : urlAnons) {
            if (StringUtils.contains(servletRequest.getRequestURI(), urlAnon)) {
                return true;
            }
        }

        // 验证token
        // 从http请求头中取出 token
        String token = servletRequest.getHeader(BaseConstant.TOKEN_HEAD);
        // 执行认证
        if (StringUtils.isBlank(token)) {
            throw new CustomException(ResultEnum.TOKEN_NOT_FOUND);
        }

        // refresh接口校验
        if (StringUtils.contains(servletRequest.getRequestURI(), BaseConstant.REFRESH_URL)) {
            // 校验并获取token，失败返回异常
            final Map<String, String> map = JWTUtil.getRefreshMap(token);
            // 添加request参数
            servletRequest.setAttribute(BaseConstant.USER_ID_STR, map.get(BaseConstant.USER_ID_STR));
            servletRequest.setAttribute(BaseConstant.USER_STR, map.get(BaseConstant.USER_STR));
            return true;
        }

        // 普通需校验的接口
        // 校验并获取token参数，失败返回异常
        final Map<String, String> map = JWTUtil.getMap(token);
        // 添加request参数
        servletRequest.setAttribute(BaseConstant.USER_ID_STR, map.get(BaseConstant.USER_ID_STR));
        servletRequest.setAttribute(BaseConstant.USER_STR, map.get(BaseConstant.USER_STR));
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest servletRequest,
                           HttpServletResponse httpServletResponse,
                           Object handler, ModelAndView modelAndView) {
        //do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest servletRequest,
                                HttpServletResponse httpServletResponse,
                                Object handler, Exception e) {
        //do nothing
    }
}
