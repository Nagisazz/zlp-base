package com.nagisazz.base.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * RestTemplate配置
 */
@Setter
@Getter
@ToString
@ConfigurationProperties("rest")
public class RestTemplateProperties {

    /**
     * 连接超时时间, unit: ms
     */
    private Integer connectTimeout = 5 * 1000;

    /**
     * 数据读取超时时间, unit: ms
     */
    private Integer readTimeout = 60 * 1000;

    /**
     * 客户端配置
     */
    private HttpClient httpClient = new HttpClient();

    @Getter
    @Setter
    @ToString
    public static class HttpClient {

        /**
         * 最大连接数
         */
        private Integer maxTotal = 200;

        /**
         * 同路由并发数
         */
        private Integer maxPreRoute = 20;

        /**
         * 连接重试次数
         */
        private Integer retryCount = 2;

        /**
         * 连接不够用的等待时间, unit: ms
         */
        private Integer waitTimeout = 300;

    }
}
