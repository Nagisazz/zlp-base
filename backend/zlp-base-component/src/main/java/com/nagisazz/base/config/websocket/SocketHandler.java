package com.nagisazz.base.config.websocket;

import com.alibaba.fastjson.JSON;
import com.nagisazz.base.config.log.LogStorage;
import com.nagisazz.base.config.websocket.message.WebSocketMessage;
import com.nagisazz.base.entity.LogRecord;
import com.nagisazz.base.property.WebSocketProperties;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SocketHandler
 */
@Slf4j
@RequiredArgsConstructor
public class SocketHandler extends AbstractWebSocketHandler {

    private static Map<Long, WebSocketSession> map = new ConcurrentHashMap<>();
    private final LogStorage logStorage;
    private final ZlpProperties.SystemProperties systemProperties;
    private final WebSocketProperties webSocketProperties;

    /**
     * 向指定的userId发送消息
     *
     * @param userId  与websocket一对一关联
     * @param message 消息
     */
    public void sendMessage(Long userId, WebSocketMessage message) {
        final WebSocketSession session = map.get(userId);
        sendMessage(session, JSON.toJSONString(message));
    }

    /**
     * 向指定的userId发送消息
     *
     * @param userId  与websocket一对一关联
     * @param message 消息
     */
    public void sendMessage(Long userId, String message) {
        logRecord("发送websocket消息", userId, message);
        log.info("发送websocket消息，userId：{}，message：{}", userId, message);
        final WebSocketSession session = map.get(userId);
        sendMessage(session, message);
    }

    /**
     * 向指定的websocket发送消息
     *
     * @param session session对象
     * @param message 消息
     */
    private void sendMessage(WebSocketSession session, String message) {
        if (checkSession(session)) {
            final TextMessage textMessage = new TextMessage(message);
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                log.error("websocket发送消息失败", e);
            }
        }
    }

    /**
     * 关闭websocket通道
     *
     * @param userId 外键
     */
    public void closeWebSocketChannel(Long userId) {
        final WebSocketSession session = map.get(userId);
        closeWebSocketChannel(session);
        map.remove(userId);
    }

    /**
     * 收到消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = null;
        for (Map.Entry<Long, WebSocketSession> entry : map.entrySet()) {
            if (session.getId().equals(entry.getValue().getId())) {
                userId = entry.getKey();
                break;
            }
        }
        logRecord("收到消息", userId, message.getPayload());
        log.info("收到websocket消息，userId：{}，sessionId：{}，sessionAddress：{}，message：{}", userId, session.getId(),
                session.getRemoteAddress(), message.getPayload());
        sendMessage(session, "收到心跳，回复正常");
        super.handleTextMessage(session, message);
    }

    /**
     * 连接建立
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final Long userId = (Long) session.getAttributes().get("userId");
        logRecord("连接建立", userId, null);
        log.info("连接已建立，userId：{}", userId);
        if (map.containsKey(userId)) {
            closeWebSocketChannel(map.get(userId));
        }
        map.put(userId, session);
        super.afterConnectionEstablished(session);
    }

    /**
     * 连接销毁
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final Long userId = (Long) session.getAttributes().get("userId");
        logRecord("连接销毁", userId, null);
        log.info("连接已销毁，userId：{}", userId);
        map.remove(userId);
        super.afterConnectionClosed(session, status);
    }

    /**
     * 关闭websocket通道.此操作不会将session从{@link SocketHandler#map}里移出
     *
     * @param session {@link WebSocketSession}
     */
    private void closeWebSocketChannel(WebSocketSession session) {
        if (checkSession(session)) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("websocket通道关闭失败", e);
            }
        }
    }

    /**
     * 校验当前系统有没有这个session且通道是否打开
     *
     * @param session session
     * @return 结果
     */
    private boolean checkSession(WebSocketSession session) {
        if (Objects.isNull(session)) {
            log.warn("session为null");
            return false;
        }
        if (!session.isOpen()) {
            log.warn("当前userId对应的websocket通道没有打开");
            return false;
        }
        return true;
    }

    /**
     * 日志记录
     *
     * @param content
     * @param userId
     * @param param
     */
    private void logRecord(String content, Long userId, String param) {
        LocalDateTime now = LocalDateTime.now();
        param = StringUtils.isBlank(param) ? null : param.replaceAll("\n", "").replaceAll("\r", "");
        logStorage.saveLog(LogRecord.builder()
                .userId(userId)
                .classify(3)
                .requestSystem(systemProperties.getName())
                .requestUrl(webSocketProperties.getSocketUrl())
                .requestParam(param)
                .requestIp(RequestUtil.getIp())
                .content(content)
                .createTime(now)
                .updateTime(now)
                .build());
    }
}
