package com.nagisazz.base.entity;

import lombok.*;

import java.time.LocalDateTime;

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
     * 文件原始类型
     */
    private String suffix;

    /**
     * 类型（1：文本，2：文档，3：图片，4：视频，5：音频，6：其他）
     */
    private Integer type;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 扩展字段
     */
    private String ext;
}