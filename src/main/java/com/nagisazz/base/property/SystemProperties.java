package com.nagisazz.base.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * system配置
 */
@Setter
@Getter
@ConfigurationProperties("system")
public class SystemProperties {

    /**
     * 登录配置
     */
    private LoginProperties login = new LoginProperties();

    /**
     * JWT配置
     */
    private JWTProperties jwt = new JWTProperties();

    @Getter
    @Setter
    @ToString
    public static class LoginProperties {

        /**
         * 是否开启登录校验
         */
        private Boolean enabled = true;

        /**
         * 系统首页地址
         */
        private String indexUrl;

        /**
         * 系统登录地址
         */
        private String loginUrl;

        /**
         * 系统登出地址
         */
        private String logoutUrl;

        /**
         * 不校验登录地址
         */
        private String permitUrl;
    }

    @Getter
    @Setter
    @ToString
    public static class JWTProperties {

        /**
         * 签名密钥
         */
        private String signature = "nagisazlp";

        /**
         * token过期时间, unit: min
         */
        private Integer expireTime = 60;
    }
}
