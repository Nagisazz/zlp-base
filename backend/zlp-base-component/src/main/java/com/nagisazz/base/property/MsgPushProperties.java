package com.nagisazz.base.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信消息推送配置
 */
@Setter
@Getter
@ToString
@ConfigurationProperties("msg")
public class MsgPushProperties {

    /**
     * url
     */
    private String url;

    /**
     * token
     */
    private String token;

    /**
     * 群组Id
     */
    private String groupId;
}
