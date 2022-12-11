package com.nagisazz.base.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.PostConstruct;

/**
 * system配置
 */
@Setter
@Getter
@ConfigurationProperties("zlp")
public class SystemProperties {

    public static JWTProperties jwtStatic;

    /**
     * 登录配置
     */
    private LoginProperties login = new LoginProperties();

    /**
     * JWT配置
     */
    private JWTProperties jwt = new JWTProperties();

    /**
     * 微信小程序配置
     */
    private WxProperties wx = new WxProperties();

    @PostConstruct
    public void setJwtStatic(){
        jwtStatic = jwt;
    }

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
        private Integer tokenExpireTime = 1;

        /**
         * refresh_token签名密钥
         */
        private String refreshTokenSignature = "nagisazlprefresh";

        /**
         * refresh_token过期时间, unit: hour
         */
        private Integer refreshTokenExpireTime = 7 * 24;
    }

    @Getter
    @Setter
    @ToString
    public static class WxProperties {

        /**
         * 微信小程序appid
         */
        private String appid;

        /**
         * 微信小程序secret
         */
        private String secret;
    }
}
