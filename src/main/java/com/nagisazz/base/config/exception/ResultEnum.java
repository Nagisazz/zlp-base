package com.nagisazz.base.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * http返回状态码及信息
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    /**
     * 状态码
     */
    SUCCESS(true, 200, "操作成功！"),

    FAIL(false, 500, "操作失败！"),

    TOKEN_NOT_FOUND(false, 400, "无token，请重新登录"),

    TOKEN_REFRESH_NOT_FOUND(false, 400, "refresh_token校验失败"),

    TOKEN_DECODE_FAIL(false, 400, "token解密失败"),

    TOKEN_REFRESH_FAIL(false, 400, "token刷新失败"),
    ;

    /**
     * 是否成功
     */
    private final Boolean success;
    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 返回信息
     */
    private final String message;
}
