package com.nagisazz.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LoginResultEnum
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum {
    /**
     * 微信小程序未注册
     */
    NOT_REGISTER(400, "微信小程序未注册"),

    /**
     * 微信小程序登录失败
     */
    WX_LOGIN_FAIL(400, "微信小程序登录失败"),

    /**
     * 用户未绑定
     */
    NOT_BOUND(204, "用户未绑定"),
    ;

    private Integer code;

    private String message;
}