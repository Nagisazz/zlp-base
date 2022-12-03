package com.nagisazz.base.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ZlpUser {
    /**
     * 主键
     */
    private Long id;

    /**
     * 登录名
     */
    private String loginid;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注册系统
     */
    private String registerSystem;

    /**
     * 最后登录ip
     */
    private String lastIp;

    /**
     * 最后登录系统
     */
    private String lastSystem;

    /**
     * 登录次数
     */
    private Integer loginNumber;

    /**
     * 扩展字段
     */
    private String ext;

    /**
     * 是否有效
     */
    private Integer valid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}