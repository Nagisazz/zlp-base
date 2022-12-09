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

    private String name;

    private String phone;

    private String avatarurl;

    private Integer valid;

    private String lastSystem;

    private LocalDateTime createTime;

    private LocalDateTime lastLoginTime;
}