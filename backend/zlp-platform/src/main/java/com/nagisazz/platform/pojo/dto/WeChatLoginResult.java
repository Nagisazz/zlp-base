package com.nagisazz.platform.pojo.dto;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信登录接口返回结果
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WeChatLoginResult {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    @JSONField(name = "session_key")
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回
     */
    @JSONField(name = "unionid")
    private String unionId;

    /**
     * 错误码
     */
    @JSONField(name = "errcode")
    private Integer errCode;

    /**
     * 错误信息
     */
    @JSONField(name = "errmsg")
    private String errMsg;

}
