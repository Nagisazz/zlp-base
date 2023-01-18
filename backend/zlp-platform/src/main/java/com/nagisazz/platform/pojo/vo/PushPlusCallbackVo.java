package com.nagisazz.platform.pojo.vo;

import lombok.*;

/**
 * UserInfoVo
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PushPlusCallbackVo {

    private Integer code;

    private String msg;
}
