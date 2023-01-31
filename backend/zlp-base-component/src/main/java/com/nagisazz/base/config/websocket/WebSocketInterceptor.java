package com.nagisazz.base.config.websocket;

import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

/**
 * WebSocketInterceptor
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        final List<String> list = request.getHeaders().get("Sec-WebSocket-Protocol");
        // 执行认证
        if (CollectionUtils.isEmpty(list)) {
            log.error("WebSocket缺少token参数");
            return false;
        }
        String token = list.get(0);
        response.getHeaders().set("Sec-WebSocket-Protocol", token);
        // 校验并获取token参数，失败返回异常
        final Map<String, String> map = JWTUtil.getMap(token);
        // 添加参数
        attributes.put(BaseConstant.USER_ID_STR, map.get(BaseConstant.USER_ID_STR));
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler, Exception exception) {
        //do nothing
    }
}
