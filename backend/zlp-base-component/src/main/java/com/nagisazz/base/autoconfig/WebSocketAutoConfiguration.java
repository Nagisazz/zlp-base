package com.nagisazz.base.autoconfig;

import com.nagisazz.base.config.log.LogStorage;
import com.nagisazz.base.config.websocket.SocketHandler;
import com.nagisazz.base.config.websocket.WebSocketInterceptor;
import com.nagisazz.base.property.WebSocketProperties;
import com.nagisazz.base.property.ZlpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * WebSocket自动配置
 */
@Slf4j
@EnableWebSocket
@AutoConfiguration(after = LogAutoConfiguration.class)
@ConditionalOnProperty(name = "websocket.enabled", havingValue = "true")
public class WebSocketAutoConfiguration implements WebSocketConfigurer {

    @Resource
    private LogStorage logStorage;

    @Autowired
    private ZlpProperties zlpProperties;

    @Resource
    private WebSocketProperties webSocketProperties;

    @Bean
    @ConditionalOnMissingBean(name = "socketHandler")
    public SocketHandler socketHandler() {
        return new SocketHandler(logStorage, zlpProperties.getSystem(), webSocketProperties);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("WebSocket初始化");
        registry.addHandler(socketHandler(), webSocketProperties.getSocketUrl()).setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor());
    }
}
