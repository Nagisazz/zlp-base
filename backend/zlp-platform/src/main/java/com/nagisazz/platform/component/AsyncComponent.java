package com.nagisazz.platform.component;

import com.nagisazz.base.entity.SystemRegister;
import com.nagisazz.base.util.MinioHelper;
import com.nagisazz.platform.enums.FileTypeEnum;
import com.nagisazz.platform.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
public class AsyncComponent {

    @Resource
    private MinioHelper minioHelper;

    /**
     * 上传缩略图
     *
     * @param file
     * @param systemRegister
     * @param userLoginId
     * @param type
     */
    @Async
    public void uploadPreview(MultipartFile file, SystemRegister systemRegister, String userLoginId, FileTypeEnum type) {
        // 图片和视频类型生成缩略图
        if (type == FileTypeEnum.IMAGE) {
            minioHelper.upload(systemRegister.getMinioBucket(), userLoginId, FileUtil.getPreviewName(Objects.requireNonNull(FilenameUtils.getName(file.getOriginalFilename()))),
                    FileUtil.genPreviewImage(file));
        } else if (type == FileTypeEnum.VIDEO) {
            minioHelper.upload(systemRegister.getMinioBucket(), userLoginId, FileUtil.getPreviewName(Objects.requireNonNull(FilenameUtils.getName(file.getOriginalFilename()))),
                    FileUtil.genPreviewVideo(file));
        }
    }
}
