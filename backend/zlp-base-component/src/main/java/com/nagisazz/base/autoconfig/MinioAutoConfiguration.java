package com.nagisazz.base.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.nagisazz.base.property.MinioProperties;
import com.nagisazz.base.util.MinioHelper;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

@Slf4j
@AutoConfiguration(after = BaseAutoConfiguration.class)
@ConditionalOnProperty(name = "minio.endpoint")
public class MinioAutoConfiguration {

    /**
     * 初始化minio
     *
     * @param minioProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient(MinioProperties minioProperties) {
        log.info("minio初始化");
        return MinioClient.builder()
                .endpoint(HttpUrl.parse(minioProperties.getEndpoint()))
                .credentials(minioProperties.getAccessKeyId(), minioProperties.getAccessKeySecret())
                .build();
    }

    /**
     * 初始化minio工具类
     *
     * @param minioClient
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MinioHelper.class)
    public MinioHelper minioHelper(MinioClient minioClient) {
        return new MinioHelper(minioClient);
    }

}
