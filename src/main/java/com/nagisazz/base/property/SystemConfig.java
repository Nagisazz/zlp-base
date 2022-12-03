package com.nagisazz.base.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * system配置
 */
@Setter
@Getter
@ConfigurationProperties("system")
public class SystemConfig {

    /**
     * 系统名称
     */
    private String name;
}
