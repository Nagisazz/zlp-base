package com.nagisazz.base.property;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * system配置
 */
@Setter
@Getter
@ConfigurationProperties("zlp")
public class ZlpProperties {

    public static JWTProperties jwtStatic;

    /**
     * 登录配置
     */
    private LoginProperties login = new LoginProperties();

    /**
     * 系统配置
     */
    private SystemProperties system = new SystemProperties();

    /**
     * JWT配置
     */
    private JWTProperties jwt = new JWTProperties();

    /**
     * log配置
     */
    private LogProperties log = new LogProperties();

    @PostConstruct
    public void setJwtStatic() {
        jwtStatic = jwt;
    }

    @Getter
    @Setter
    @ToString
    public static class SystemProperties {

        /**
         * 系统注册标识
         */
        private String name;

        /**
         * 平台地址，ex：http://ip:port
         */
        private String platformUrl;
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
         * 不校验登录的地址，用英文逗号分割，地址需包含context-path
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

    @Getter
    @Setter
    @ToString
    public static class LogProperties {

        /**
         * 是否开启日志记录。默认开启
         */
        private Boolean enabled = true;

        /**
         * 每次发送日志记录大小。默认50
         */
        private Integer listSize = 50;

        /**
         * 日志存储缓冲队列大小。默认100
         */
        private Integer queueLimit = 100;

        /**
         * 定时存储缓冲队列时间间隔, unit: sec。默认60s
         */
        private Integer storageInterval = 60;

        /**
         * 定时发送日志时间间隔, unit: sec。默认10min
         */
        private Integer sendInterval = 10 * 60;

        /**
         * 定时发送日志线程池出错时延迟发送时间间隔, unit: ms。默认1h
         */
        private Integer delayInterval = 60 * 60 * 1000;

        /**
         * 不记录日志的地址，用英文逗号分割，地址需包含context-path
         */
        private String permitUrl;
    }
}
