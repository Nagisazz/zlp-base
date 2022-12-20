package com.nagisazz.platform.pojo.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String name;

    private String phone;

    private String avatarUrl;

    private Integer valid;

    private String lastIp;

    private String lastSystem;

    private LocalDateTime createTime;

    private LocalDateTime lastLoginTime;

}