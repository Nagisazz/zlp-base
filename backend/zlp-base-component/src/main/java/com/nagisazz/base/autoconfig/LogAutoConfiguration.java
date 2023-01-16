package com.nagisazz.base.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import com.nagisazz.base.config.log.AsyncSendLogHandler;
import com.nagisazz.base.config.log.LogQueue;
import com.nagisazz.base.config.log.LogRecordFilter;
import com.nagisazz.base.config.log.LogStorage;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.RestHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfiguration(after = BaseAutoConfiguration.class)
@ConditionalOnProperty(name = "zlp.log.enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {

    /**
     * 初始化log缓冲队列
     *
     * @param zlpProperties
     * @return
     */
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean(LogQueue.class)
    public LogQueue logQueue(ZlpProperties zlpProperties) {
        return new LogQueue(zlpProperties.getLog().getQueueLimit());
    }

    /**
     * 初始化异步发送日志处理
     *
     * @param logQueue
     * @param restHelper
     * @param zlpProperties
     * @return
     */
    @Bean(initMethod = "init")
    @DependsOn("restHelper")
    public AsyncSendLogHandler asyncSendLogHandler(LogQueue logQueue, RestHelper restHelper, ZlpProperties zlpProperties) {
        return new AsyncSendLogHandler(logQueue, restHelper, zlpProperties);
    }

    /**
     * 初始化日志存储
     *
     * @param logQueue
     * @param asyncSendLogHandler
     * @param zlpProperties
     * @return
     */
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean(LogStorage.class)
    public LogStorage logStorage(LogQueue logQueue, AsyncSendLogHandler asyncSendLogHandler, ZlpProperties zlpProperties) {
        return new LogStorage(logQueue, asyncSendLogHandler, zlpProperties.getLog());
    }

    /**
     * 初始化日志过滤器
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(LogStorage.class)
    public FilterRegistrationBean<LogRecordFilter> logFilterRegistration(LogStorage logStorage, ZlpProperties zlpProperties) {
        log.info("日志过滤器初始化");
        FilterRegistrationBean<LogRecordFilter> registration = new FilterRegistrationBean<>();
        // 注入过滤器
        registration.setFilter(new LogRecordFilter(logStorage, zlpProperties));
        // 拦截规则
        registration.addUrlPatterns("/*");
        // 过滤器名称
        registration.setName("logRecordFilter");
        // 过滤器顺序
        registration.setOrder(0);
        return registration;
    }
}