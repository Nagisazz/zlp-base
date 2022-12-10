package com.nagisazz.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class ZlpPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZlpPlatformApplication.class, args);
    }
}
