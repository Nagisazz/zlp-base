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

    private String avatarUrl;

    private String nickName;

}
