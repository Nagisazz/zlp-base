package com.nagisazz.base.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件存储路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 所有者id，zlp_user表主键，此列无值说明是访客用户上传
     */
    private Long ownerId;

    /**
     * 上传人ip
     */
    private String uploaderIp;

    /**
     * 文件所属系统标识
     */
    private String systemId;

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