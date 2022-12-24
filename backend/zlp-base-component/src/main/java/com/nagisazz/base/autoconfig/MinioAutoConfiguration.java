package com.nagisazz.base.autoconfig;

import com.nagisazz.base.property.MinioProperties;
import com.nagisazz.base.util.MinioHelper;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "minio.endpoint")
public class MinioAutoConfiguration {

    /**
     * 初始化minio
     *
     * @param minioProperties
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient(MinioProperties minioProperties) throws InvalidPortException, InvalidEndpointException {
        log.info("minio初始化");
        return new MinioClient(minioProperties.getEndpoint(), minioProperties.getAccessKeyId(),
                minioProperties.getAccessKeySecret());
    }

    /**
     * 初始化minio工具类
     *
     * @param minioClient
     * @param minioProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MinioHelper.class)
    public MinioHelper minioHelper(MinioClient minioClient, MinioProperties minioProperties) {
        return new MinioHelper(minioClient, minioProperties);
    }

}
