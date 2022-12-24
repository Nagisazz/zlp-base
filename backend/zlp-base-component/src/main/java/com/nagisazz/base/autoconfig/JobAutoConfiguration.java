package com.nagisazz.base.autoconfig;

import com.nagisazz.base.property.JobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "job.addresses")
public class JobAutoConfiguration {

    /**
     * 初始化job
     *
     * @param jobProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
    public XxlJobSpringExecutor xxlJobExecutor(JobProperties jobProperties) {
        log.info("job初始化");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobProperties.getAddresses());
        xxlJobSpringExecutor.setAccessToken(jobProperties.getAccessToken());
        xxlJobSpringExecutor.setAppname(jobProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(jobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(jobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(jobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(jobProperties.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(jobProperties.getExecutor().getLogretentiondays());
        return xxlJobSpringExecutor;
    }
}
