package com.nagisazz.platform.pojo.dto;

import lombok.*;

/**
 * PushPlusCallbackParam
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PushPlusCallbackParam {

    private String event;

    private MessageInfo messageInfo;

    @Setter
    @Getter
    public static class MessageInfo {

        private String message;

        private String shortCode;

        private Integer sendStatus;
    }
}
