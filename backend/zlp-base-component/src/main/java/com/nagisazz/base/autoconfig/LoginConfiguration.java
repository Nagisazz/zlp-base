package com.nagisazz.base.autoconfig;

import com.nagisazz.base.config.interceptor.AuthorizationInterceptor;
import com.nagisazz.base.property.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "zlp.login.enabled", havingValue = "true", matchIfMissing = true)
public class LoginConfiguration implements WebMvcConfigurer {

    @Autowired
    private SystemProperties systemProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("登录拦截器初始化");
        registry.addInterceptor(new AuthorizationInterceptor(systemProperties)).addPathPatterns("/**");
    }
}
