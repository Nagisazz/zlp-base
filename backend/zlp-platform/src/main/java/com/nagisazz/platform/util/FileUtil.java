package com.nagisazz.platform.util;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.drew.metadata.mp3.Mp3Directory;
import com.drew.metadata.mp4.Mp4Directory;
import com.google.common.collect.Lists;
import com.nagisazz.platform.enums.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class FileUtil {

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
            FileTypeEnum fileType = getFileType(file.getContentType());
            // 文档和其他类型不做处理
            if (fileType == FileTypeEnum.DOC || fileType == FileTypeEnum.OTHER) {
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
    public static FileTypeEnum getFileType(String fileMimeType) {
        if (DOC_TYPES.contains(fileMimeType)) {
            return FileTypeEnum.DOC;
        } else if (fileMimeType.startsWith("image")) {
            return FileTypeEnum.IMAGE;
        } else if (fileMimeType.startsWith("video")) {
            return FileTypeEnum.VIDEO;
        } else if (fileMimeType.startsWith("audio")) {
            return FileTypeEnum.AUDIO;
        } else {
            return FileTypeEnum.OTHER;
        }
    }

    /**
     * 生成缩略图文件名
     *
     * @param fileName
     * @return
     */
    public static String getPreviewName(String fileName) {
        String[] filename = fileName.split("\\.");
        return filename[0] + "_preview.jpg";
    }

    /**
     * 生成图片缩略图
     *
     * @param multipartFile
     * @return
     */
    public static InputStream genPreviewImage(MultipartFile multipartFile) {
        File file = null;
        InputStream res = null;
        try {
            String originalFilename = FilenameUtils.getName(multipartFile.getOriginalFilename());
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], ".jpg");
            // 生成缩略图
            Thumbnails.of(multipartFile.getInputStream()).scale(0.25).toFile(file);
            res = Files.newInputStream(file.toPath());
        } catch (Exception e) {
            log.error("生成图片缩略图失败", e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
        return res;
    }

    /**
     * 生成视频缩略图
     *
     * @param file
     * @return
     */
    public static InputStream genPreviewVideo(MultipartFile file) {
        File fileOrigin = null;
        File target = null;
        InputStream res = null;
        try {
            String originalFilename = FilenameUtils.getName(file.getOriginalFilename());
            String[] filename = originalFilename.split("\\.");
            fileOrigin = File.createTempFile(filename[0], filename[1]);
            target = File.createTempFile(filename[0], ".jpg");
            file.transferTo(fileOrigin);

            MultimediaObject multimediaObject = new MultimediaObject(fileOrigin);
            ScreenExtractor screenExtractor = new ScreenExtractor();
            int width = -1;
            int height = -1;
            // 视频第1s的截图
            long millis = 1000;
            int quality = 10;
            screenExtractor.renderOneImage(multimediaObject, width, height, millis, target, quality);
            res = Files.newInputStream(target.toPath());
        } catch (Exception e) {
            log.error("生成视频缩略图失败", e);
        } finally {
            if (fileOrigin != null) {
                fileOrigin.delete();
            }
            if (target != null) {
                target.delete();
            }
        }
        return res;
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
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(FileSystemDirectory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(FileSystemDirectory.class).getTags());
            }
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(FileTypeDirectory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(FileTypeDirectory.class).getTags());
            }
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class).getTags());
            }
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(Mp4Directory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(Mp4Directory.class).getTags());
            }
            if (!Objects.isNull(metadata.getFirstDirectoryOfType(Mp3Directory.class))) {
                tags.addAll(metadata.getFirstDirectoryOfType(Mp3Directory.class).getTags());
            }
            tags.forEach(tag -> meta.put(tag.getTagName(), tag.getDescription()));
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
        }
        return meta;
    }
}
