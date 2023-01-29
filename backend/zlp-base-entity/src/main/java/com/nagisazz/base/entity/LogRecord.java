package com.nagisazz.base.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogRecord {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 日志类型，1：登录，2：操作，3：连接
     */
    private Integer classify;

    /**
     * 请求系统
     */
    private String requestSystem;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 返回结果内容
     */
    private String responseResult;

    /**
     * 日志内容
     */
    private String content;

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
}