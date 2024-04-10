package com.nagisazz.platform.util;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class FileMetaUtil {

    private static final List<String> DOC_TYPES = Lists.newArrayList();

    static {
        DOC_TYPES.add("application/pdf");
        DOC_TYPES.add("application/xml");
        DOC_TYPES.add("application/rss+xml");
        DOC_TYPES.add("application/atom+xml");
        DOC_TYPES.add("application/msword");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        DOC_TYPES.add("application/vnd.ms-word.template.macroenabled.12");
        DOC_TYPES.add("application/vnd.ms-word.document.macroenabled.12");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.presentationml.slide");
        DOC_TYPES.add("application/vnd.openxmlformats-officedocument.presentationml.template");
        DOC_TYPES.add("application/vnd.ms-works");
        DOC_TYPES.add("application/vnd.ms-wpl");
        DOC_TYPES.add("application/vnd.ms-xpsdocument");
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @return
     */
    public static String getMeta(MultipartFile file) {
        try {
            int fileType = getFileType(file.getContentType());
            // 文档和其他类型不做处理
            if (fileType == 2 || fileType == 6) {
                return null;
            }
            Map<String, Object> meta = getMeta(file.getInputStream());
            if (MapUtils.isNotEmpty(meta)) {
                return JSON.toJSONString(meta);
            }
        } catch (IOException e) {
            log.error("获取文件信息失败", e);
        }
        return null;
    }

    /**
     * 获取文件类型，2：文档，3：图片，4：视频，5：音频，6：其他
     *
     * @param fileMimeType
     * @return
     */
    public static Integer getFileType(String fileMimeType) {
        if (DOC_TYPES.contains(fileMimeType)) {
            return 2;
        } else if (fileMimeType.startsWith("image")) {
            return 3;
        } else if (fileMimeType.startsWith("video")) {
            return 4;
        } else if (fileMimeType.startsWith("audio")) {
            return 5;
        } else {
            return 6;
        }
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @return
     */
    private static Map<String, Object> getMeta(InputStream file) {
        Map<String, Object> meta = new HashMap<>();
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            // 不支持的文件类型
            if (Objects.isNull(metadata)) {
                return null;
            }
            List<Tag> tags = Lists.newArrayList();
            metadata.getDirectories().forEach(directory -> {
                tags.addAll(directory.getTags());
            });
            tags.forEach(tag -> meta.put(tag.getTagName(), tag.getDescription()));
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
        }
        return meta;
    }
}
