package com.nagisazz.base.util;

import com.nagisazz.base.config.constants.BaseConstant;
import com.nagisazz.base.config.msg.MsgPushVo;
import com.nagisazz.base.property.MsgPushProperties;
import com.nagisazz.base.property.ZlpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * 消息推送工具类
 */
@Slf4j
@RequiredArgsConstructor
public class MsgPushHelper {

    private final RestHelper restHelper;

    private final MsgPushProperties msgPushProperties;

    private final ZlpProperties.SystemProperties systemProperties;

    /**
     * 以默认参数发送群组消息
     *
     * @param title
     * @param content
     */
    public void sendGroupMsg(String title, String content) {
        sendGroupMsg(title, content, msgPushProperties.getMsgType());
    }

    /**
     * 以指定消息类型发送群组消息
     *
     * @param title
     * @param content
     */
    public void sendGroupMsg(String title, String content, Integer msgType) {
        sendGroupMsg(title, content, msgPushProperties.getTopic(), msgType);
    }

    /**
     * 发送给指定群组消息
     *
     * @param title
     * @param content
     * @param group
     */
    public void sendGroupMsg(String title, String content, String group, Integer msgType) {
        final MsgPushVo msgPushVo = buildCommon(title, content);
        msgPushVo.setTo(null);
        msgPushVo.setTopic(group);
        msgPushVo.setMsgType(msgType);
        sendMsg(msgPushVo);
    }

    /**
     * 以默认参数发送个人消息
     *
     * @param title
     * @param content
     */
    public void sendFriendMsg(String title, String content) {
        sendFriendMsg(title, content, msgPushProperties.getMsgType());
    }

    /**
     * 以指定消息类型发送个人消息
     *
     * @param title
     * @param content
     */
    public void sendFriendMsg(String title, String content, Integer msgType) {
        sendFriendMsg(title, content, msgPushProperties.getTo(), msgType);
    }

    /**
     * 发送给指定人消息
     *
     * @param title
     * @param content
     * @param receiver
     */
    public void sendFriendMsg(String title, String content, String receiver, Integer msgType) {
        final MsgPushVo msgPushVo = buildCommon(title, content);
        msgPushVo.setTo(receiver);
        msgPushVo.setTopic(null);
        msgPushVo.setMsgType(msgType);
        sendMsg(msgPushVo);
    }

    private MsgPushVo buildCommon(String title, String content) {
        MsgPushVo msgPushVo = MsgPushVo.builder().build();
        BeanUtils.copyProperties(msgPushProperties, msgPushVo);
        msgPushVo.setSystem(systemProperties.getName());
        msgPushVo.setTitle(title);
        msgPushVo.setContent(content);
        return msgPushVo;
    }

    private void sendMsg(MsgPushVo msgPushVo) {
        restHelper.post(systemProperties.getPlatformUrl() + BaseConstant.MSG_SEND_URL, msgPushVo);
    }
}
