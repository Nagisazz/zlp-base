package com.nagisazz.base.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Logback配置
 */
@Setter
@Getter
@ToString
@ConfigurationProperties("logback.level")
public class LogbackProperties {

    /**
     * 日志输出根级别
     */
    private String rootLevel = "INFO";

    /**
     * SQL日志输出级别
     */
    private String sqlLevel = "INFO";
}
