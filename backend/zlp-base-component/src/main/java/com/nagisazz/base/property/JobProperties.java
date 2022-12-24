package com.nagisazz.base.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * job配置
 */
@Setter
@Getter
@ToString
@ConfigurationProperties("job")
public class JobProperties {

    /**
     * job后台管理地址
     */
    private String addresses;

    /**
     * token
     */
    private String accessToken;

    /**
     * 执行器配置
     */
    private Executor executor;

    @Getter
    @Setter
    @ToString
    public static class Executor {

        /**
         * 名称
         */
        private String appname;

        /**
         * 地址
         */
        private String address;

        /**
         * ip
         */
        private String ip;

        /**
         * 端口
         */
        private Integer port;

        /**
         * 日志路径
         */
        private String logpath;

        /**
         * 日志保留天数
         */
        private Integer logretentiondays = 10;
    }
}
