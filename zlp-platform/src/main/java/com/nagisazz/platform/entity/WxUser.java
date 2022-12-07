package com.nagisazz.platform.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WxUser {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String wxid;

    /**
     * 
     */
    private String openid;

    /**
     * 
     */
    private String unionid;

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private Integer gender;

    /**
     * 
     */
    private String country;

    /**
     * 
     */
    private String province;

    /**
     * 
     */
    private String city;

    /**
     * 
     */
    private String avatarurl;

    /**
     * 
     */
    private Integer loginNumber;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime lastTime;
}