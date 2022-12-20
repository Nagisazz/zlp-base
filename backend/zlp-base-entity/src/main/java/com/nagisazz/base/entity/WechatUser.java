package com.nagisazz.base.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WechatUser {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 微信id
     */
    private String wxid;

    /**
     * 小程序id
     */
    private String openid;

    /**
     * 联合id
     */
    private String unionid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 头像url
     */
    private String avatarurl;

    /**
     * 登录次数
     */
    private Integer loginNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}