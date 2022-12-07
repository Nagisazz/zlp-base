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
@ConfigurationProperties("zlp")
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
         * sso系统地址
         */
        private String ssoUrl;

        /**
         * 不校验登录的地址，地址需包含context-path
         */
        private String permitUrl;
    }

    @Getter
    @Setter
    @ToString
    public static class JWTProperties {

        /**
         * token签名密钥
         */
        private String tokenSignature = "nagisazlp";

        /**
         * token过期时间, unit: min
         */
        private Integer tokenExpireTime = 60;

        /**
         * refresh_token签名密钥
         */
        private String refreshTokenSignature = "nagisazlprefresh";

        /**
         * refresh_token过期时间, unit: hour
         */
        private Integer refreshTokenExpireTime = 7 * 24;
    }
}
