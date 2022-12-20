package com.nagisazz.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ValidEnum
 */
@Getter
@AllArgsConstructor
public enum ValidEnum {
    /**
     * 无效
     */
    INVALID(0, "无效"),

    /**
     * 有效
     */
    VALID(1, "有效");

    private Integer code;

    private String message;

}