package com.nagisazz.base.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SystemRegister {
    /**
     * 主键
     */
    private Long id;

    /**
     * 系统标识
     */
    private String identifier;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统描述
     */
    private String description;

    /**
     * 系统首页地址
     */
    private String indexUrl;

    /**
     * 是否有效
     */
    private Integer valid;

    /**
     * 扩展字段
     */
    private String ext;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}