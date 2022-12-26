package com.nagisazz.base.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * minio配置
 */
@Setter
@Getter
@ToString
@ConfigurationProperties("minio")
public class MinioProperties {

    /**
     * minio路径
     */
    private String endpoint;

    /**
     * key
     */
    private String accessKeyId;

    /**
     * secret
     */
    private String accessKeySecret;

}
