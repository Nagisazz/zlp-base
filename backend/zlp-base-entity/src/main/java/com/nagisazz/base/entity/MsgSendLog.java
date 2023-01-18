package com.nagisazz.base.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MsgSendLog {
    /**
     * 主键
     */
    private Long id;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 标题
     */
    private String title;

    /**
     * 接收群组id
     */
    private String receiveGroup;

    /**
     * 接收好友id
     */
    private String receiveFriend;

    /**
     * 模板类型
     */
    private String msgTemplate;

    /**
     * 发送类型
     */
    private String msgChannel;

    /**
     * 发送状态，-1：发送调用失败，0：未发送，1：发送中，2：发送成功，3：发送失败
     */
    private Integer status;

    /**
     * 错误内容
     */
    private String failContent;

    /**
     * 发送系统
     */
    private String sendSystem;

    /**
     * 消息类型，1：基金定投
     */
    private Integer msgType;

    /**
     * 扩展字段
     */
    private String ext;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 内容
     */
    private String content;
}