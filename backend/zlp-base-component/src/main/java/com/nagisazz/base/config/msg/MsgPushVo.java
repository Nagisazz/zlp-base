package com.nagisazz.base.config.msg;

import lombok.*;

/**
 * MsgPushVo
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MsgPushVo {

    /**
     * 发送系统标识
     */
    private String system;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * token
     */
    private String token;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 群组Id
     */
    private String topic;

    /**
     * 好友令牌，微信公众号渠道填写好友令牌，企业微信渠道填写企业微信用户id
     */
    private String to;

    /**
     * 发送模板
     *
     * @see MsgPushEnum.MsgTemplateEnum
     */
    private String template = "html";

    /**
     * 发送渠道
     *
     * @see MsgPushEnum.MsgChannelEnum
     */
    private String channel = "wechat";

    /**
     * webhook编码
     */
    private String webhook;

    /**
     * callbackUrl
     */
    private String callbackUrl;

    /**
     * 毫秒时间戳，服务器时间戳大于此时间戳，则消息不会发送
     */
    private Long timestamp;
}
