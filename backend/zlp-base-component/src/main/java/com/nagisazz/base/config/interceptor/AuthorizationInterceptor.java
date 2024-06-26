package com.nagisazz.base.config.interceptor;

import com.github.pagehelper.PageHelper;
import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.config.exception.CustomException;
import com.nagisazz.base.config.page.PageParam;
import com.nagisazz.base.config.page.PageSupport;
import com.nagisazz.base.config.response.WrapResp;
import com.nagisazz.base.enums.ResultEnum;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.JWTUtil;
import com.nagisazz.base.util.SqlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * token验证
 **/
@Slf4j
@RequiredArgsConstructor
public class AuthorizationInterceptor implements AsyncHandlerInterceptor {

    private final ZlpProperties zlpProperties;

    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                             Object object) {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        // 从http请求头中取出 token
        String token = servletRequest.getHeader(BaseConstant.TOKEN_HEAD);

        // 判断是否需要进行自动填充分页参数
        HandlerMethod handlerMethod = (HandlerMethod) object;
        WrapResp wrapResp = handlerMethod.getMethodAnnotation(WrapResp.class);
        if (wrapResp == null) {
            wrapResp = handlerMethod.getClass().getAnnotation(WrapResp.class);
        }
        boolean autoWrapPage = false;
        if (wrapResp != null) {
            autoWrapPage = wrapResp.autoWrapPage();
        }
        if (autoWrapPage) {
            PageParam pageParam = PageSupport.buildPageRequest();
            Integer pageNum = pageParam.getPageNum();
            Integer pageSize = pageParam.getPageSize();
            if (!Objects.isNull(pageNum) && !Objects.isNull(pageSize)) {
                String orderBy = SqlUtil.escapeOrderBySql(pageParam.getOrderBy());
                // 进行分页
                PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(true).setPageSizeZero(true);
            }
        }

        // 不校验放开且没有token的地址
        if (StringUtils.isNotBlank(zlpProperties.getLogin().getPermitUrl())) {
            final List<String> urlAnons = Arrays.stream(StringUtils.split(zlpProperties.getLogin().getPermitUrl(), ","))
                    .map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            for (String urlAnon : urlAnons) {
                if (StringUtils.contains(servletRequest.getRequestURI(), urlAnon) && StringUtils.isBlank(token)) {
                    return true;
                }
            }
        }

        // 验证token
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
        // do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest servletRequest,
                                HttpServletResponse httpServletResponse,
                                Object handler, Exception e) {
        // do nothing
    }
}
