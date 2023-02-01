package com.nagisazz.platform.pojo.vo;

import lombok.*;

import java.time.LocalDateTime;

/**
 * UserInfoVo
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {

    private String token;

    private String refreshToken;

    private String loginId;

    private String name;

    private String phone;

    private String mail;

    private String avatarurl;

    private Integer valid;

    private String lastIp;

    private String lastSystem;

    private LocalDateTime createTime;

    private LocalDateTime lastLoginTime;

}