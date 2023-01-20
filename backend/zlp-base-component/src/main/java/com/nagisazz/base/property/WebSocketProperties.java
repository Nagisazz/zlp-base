package com.nagisazz.base.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSocket配置，应同步开启log配置
 */
@Setter
@Getter
@ToString
@ConfigurationProperties("websocket")
public class WebSocketProperties {

    /**
     * 是否开启WebSocket，默认关闭
     */
    private Boolean enabled = false;

    /**
     * websocket连接地址，默认/websocket/start
     */
    private String socketUrl = "/websocket/start";
}
