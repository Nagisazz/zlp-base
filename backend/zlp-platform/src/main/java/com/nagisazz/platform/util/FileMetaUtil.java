package com.nagisazz.platform.util;

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
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class FileMetaUtil {

    /**
     * 获取文件信息
     *
     * @param file
     * @return
     */
    public static String getMeta(MultipartFile file) {
        try {
            Map<String, Object> meta = getMeta(transferToFile(file), getFileType(file.getContentType()));
            if (MapUtils.isNotEmpty(meta)) {
                return JSON.toJSONString(meta);
            }
        } catch (IOException e) {
            log.error("获取文件信息失败", e);
        }
        return null;
    }

    /**
     * 转换文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    private static File transferToFile(MultipartFile multipartFile) throws IOException {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[1] + ".");
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            throw e;
        }
        return file;
    }

    /**
     * 获取文件类型
     *
     * @param fileMimeType
     * @return
     */
    private static Integer getFileType(String fileMimeType) {
        if (StringUtils.isBlank(fileMimeType)) {
            return 0;
        }
        if (fileMimeType.startsWith("image")) {
            return 1;
        } else if (fileMimeType.startsWith("video")) {
            return 2;
        } else if (fileMimeType.startsWith("audio")) {
            return 3;
        }
        return 0;
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
        Metadata metadata = null;
        try {
            if (fileType == 1) {
                metadata = ImageMetadataReader.readMetadata(file);
            } else if (fileType == 2) {
                metadata = Mp4MetadataReader.readMetadata(file);
            } else if (fileType == 3) {
                metadata = Mp3MetadataReader.readMetadata(file);
            }
            // 不支持的文件类型
            if (Objects.isNull(metadata)) {
                return null;
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
