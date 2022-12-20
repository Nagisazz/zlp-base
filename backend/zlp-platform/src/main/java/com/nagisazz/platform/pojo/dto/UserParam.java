package com.nagisazz.platform.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserParam {

    private Integer type;

    private String code;

    private String systemId;

    private String loginId;

    private String name;

    private String password;

    private String phone;

    private String mail;

    private String avatarUrl;

}
