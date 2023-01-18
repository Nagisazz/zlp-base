package com.nagisazz.base.util;

import com.nagisazz.base.property.MsgPushProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * 消息推送工具类
 */
@Slf4j
@RequiredArgsConstructor
public class MsgPushHelper {

    private final RestHelper restHelper;

    private final MsgPushProperties msgPushProperties;

    /**
     * 发送群组消息
     * @param title
     * @param content
     */
    public void sendMsg(String title, String content) {
        String sendMsgUrl = MessageFormat.format(msgPushProperties.getUrl(), msgPushProperties.getToken(), title, content, msgPushProperties.getGroupId());
        restHelper.get(sendMsgUrl);
    }
}
