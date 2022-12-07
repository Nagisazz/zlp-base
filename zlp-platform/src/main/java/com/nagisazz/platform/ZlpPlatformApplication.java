package com.nagisazz.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.nagisazz.platform.dao")
public class ZlpPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZlpPlatformApplication.class, args);
    }
}
