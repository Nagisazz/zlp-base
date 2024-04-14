package com.nagisazz.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FileTypeEnum
 */
@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    DOC(2, "文档"),

    IMAGE(3, "图片"),

    VIDEO(4, "视频"),

    AUDIO(5, "音频"),

    OTHER(6, "其他"),

    ;

    private Integer code;

    private String desc;
}