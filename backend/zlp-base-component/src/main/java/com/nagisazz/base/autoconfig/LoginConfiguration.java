package com.nagisazz.base.autoconfig;

import com.nagisazz.base.config.interceptor.AuthorizationInterceptor;
import com.nagisazz.base.property.ZlpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 登录校验自动配置
 */
@Slf4j
@AutoConfiguration(after = BaseAutoConfiguration.class)
@ConditionalOnProperty(name = "zlp.login.enabled", havingValue = "true", matchIfMissing = true)
public class LoginConfiguration implements WebMvcConfigurer {

    @Autowired
    private ZlpProperties zlpProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("登录拦截器初始化");
        registry.addInterceptor(new AuthorizationInterceptor(zlpProperties)).addPathPatterns("/**");
    }
}
