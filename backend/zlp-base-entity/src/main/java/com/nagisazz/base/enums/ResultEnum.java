package com.nagisazz.base.enums;

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

    INFO(true, 201, "操作成功！"),

    FAIL(false, 500, "操作失败！"),

    PARAM_FAIL(false, 400, "参数有误！"),

    TOKEN_NOT_FOUND(false, 401, "无token，请重新登录"),

    TOKEN_DECODE_FAIL(false, 401, "token解密失败"),

    TOKEN_EXPIRED_FAIL(false, 412, "token过期"),
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
