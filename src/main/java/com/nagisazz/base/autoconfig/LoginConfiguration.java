package com.nagisazz.base.autoconfig;

import com.nagisazz.base.config.interceptor.AuthorizationInterceptor;
import com.nagisazz.base.property.SystemProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@ConditionalOnProperty(name = "zlp.login.captcha.enabled", havingValue = "true", matchIfMissing = true)
public class LoginConfiguration implements WebMvcConfigurer {

    @Resource
    private SystemProperties systemProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizationInterceptor(systemProperties)).addPathPatterns("/**");
    }
}
