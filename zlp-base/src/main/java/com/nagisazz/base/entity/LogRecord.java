package com.nagisazz.base.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
     * 请求ip
     */
    private String requestIp;

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