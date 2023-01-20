package com.nagisazz.base.autoconfig;

import com.nagisazz.base.property.MsgPushProperties;
import com.nagisazz.base.property.ZlpProperties;
import com.nagisazz.base.util.MsgPushHelper;
import com.nagisazz.base.util.RestHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * 消息推送自动配置
 */
@Slf4j
@AutoConfiguration(after = BaseAutoConfiguration.class)
@ConditionalOnProperty(name = "msg.url")
public class MsgPushAutoConfiguration {

    /**
     * 初始化MsgPushHelper
     *
     * @param restHelper
     * @param msgPushProperties
     * @return
     */
    @Bean
    @DependsOn("restHelper")
    @ConditionalOnMissingBean(MsgPushHelper.class)
    public MsgPushHelper msgPushHelper(RestHelper restHelper, MsgPushProperties msgPushProperties, ZlpProperties zlpProperties) {
        log.info("MsgPushHelper初始化");
        return new MsgPushHelper(restHelper, msgPushProperties, zlpProperties.getSystem());
    }
}
