package com.nagisazz.base.util;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.mp3.Mp3MetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class FileMetaUtil {

    /**
     * 获取图片文件信息
     *
     * @param file
     * @return
     */
    public static String getImageMeta(File file) {
        return JSON.toJSONString(getMeta(file, 1));
    }

    /**
     * 获取视频文件信息
     *
     * @param file
     * @return
     */
    public static String getVideoMeta(File file) {
        return JSON.toJSONString(getMeta(file, 2));
    }

    /**
     * 获取音频文件信息
     *
     * @param file
     * @return
     */
    public static String getAudioMeta(File file) {
        return JSON.toJSONString(getMeta(file, 3));
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @param fileType 1:图片，2视频，3音频
     * @return
     */
    private static Map<String, Object> getMeta(File file, Integer fileType) {
        Map<String, Object> meta = new HashMap<>();
        Metadata metadata;
        try {
            if (fileType == 1) {
                metadata = ImageMetadataReader.readMetadata(file);
            } else if (fileType == 2) {
                metadata = Mp4MetadataReader.readMetadata(file);
            } else {
                metadata = Mp3MetadataReader.readMetadata(file);
            }
            List<Tag> tags = Lists.newArrayList();
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(FileSystemDirectory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(FileSystemDirectory.class).getTags());
            }
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(FileTypeDirectory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(FileTypeDirectory.class).getTags());
            }
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class).getTags());
            }
            tags.forEach(tag -> meta.put(tag.getTagName(), tag.getDescription()));
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
        }
        return meta;
    }
}
