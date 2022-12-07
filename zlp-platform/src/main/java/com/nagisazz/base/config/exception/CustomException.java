package com.nagisazz.base.config.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * 自定义异常
 */

@Getter
@ToString
public class CustomException extends RuntimeException {
    /**
     * 是否成功
     */
    private final boolean success = false;
    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 异常信息
     */
    private final String message;

    public CustomException(final ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public CustomException(final ResultEnum resultEnum, Throwable cause) {
        super(resultEnum.getMessage(), cause);
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public CustomException(final Integer code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CustomException(final Integer code, final String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 构建
     *
     * @param resultEnum 状态枚举
     */
    public static CustomException create(final ResultEnum resultEnum) {
        return new CustomException(resultEnum);
    }

    /**
     * 构建
     *
     * @param code    {@linkplain ResultEnum#getCode()}
     * @param message {@linkplain ResultEnum#getMessage()}
     * @return CustomException
     */
    public static CustomException create(final Integer code, final String message) {
        return new CustomException(code, message);
    }
}

