package com.nagisazz.base.property;

import com.nagisazz.base.config.msg.MsgPushEnum;
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
     * 是否开启消息推送，默认关闭
     */
    private Boolean enabled = false;

    /**
     * url
     */
    private String url = "http://www.pushplus.plus/send";

    /**
     * 默认消息类型
     */
    private Integer msgType;

    /**
     * token
     */
    private String token;

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
