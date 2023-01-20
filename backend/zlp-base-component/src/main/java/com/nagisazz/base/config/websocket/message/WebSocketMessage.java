package com.nagisazz.base.config.websocket.message;


import lombok.*;

/**
 * 统一封装的websocket消息结构
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {

    /**
     * WebSocket类型
     */
    private Integer code;

    /**
     * 消息体
     */
    private MessageData data;
}
