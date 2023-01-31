package com.nagisazz.base.config.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MsgPushEnum {

    /**
     * 消息发送模板枚举
     */
    @Getter
    @AllArgsConstructor
    public enum MsgTemplateEnum {

        HTML("html", "默认模板，支持html文本"),

        TXT("txt", "纯文本展示，不转义html"),

        JSON("json", "内容基于json格式展示"),

        MARKDOWN("markdown", "内容基于markdown格式展示"),

        CLOUDMONITOR("cloudMonitor", "阿里云监控报警定制模板"),

        JENKINS("jenkins", "jenkins插件定制模板"),

        ROUTE("route", "路由器插件定制模板"),

        PAY("pay", "支付成功通知模板"),
        ;

        private String name;

        private String desc;
    }

    /**
     * 消息发送渠道枚举
     */
    @Getter
    @AllArgsConstructor
    public enum MsgChannelEnum {

        WECHAT("wechat", "微信公众号"),

        WEBHOOK("webhook", "第三方webhook"),

        CP("cp", "企业微信应用"),

        MAIL("mail", "邮箱"),

        SMS("sms", "短信"),
        ;

        private String name;

        private String desc;
    }
}
