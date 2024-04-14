package com.nagisazz.platform.service;

import com.nagisazz.base.dao.FileInfoExtendMapper;
import com.nagisazz.base.entity.FileInfo;
import com.nagisazz.base.entity.SystemRegister;
import com.nagisazz.base.entity.ZlpUser;
import com.nagisazz.base.enums.ValidEnum;
import com.nagisazz.base.util.CommonWebUtil;
import com.nagisazz.base.util.MinioHelper;
import com.nagisazz.base.util.RequestUtil;
import com.nagisazz.platform.cache.SystemRegisterCache;
import com.nagisazz.platform.component.AsyncComponent;
import com.nagisazz.platform.enums.FileTypeEnum;
import com.nagisazz.platform.pojo.dto.FileParam;
import com.nagisazz.platform.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    @Resource
    private FileInfoExtendMapper fileInfoExtendMapper;

    @Resource
    private MinioHelper minioHelper;

    @Resource
    private SystemRegisterCache systemRegisterCache;

    @Resource
    private AsyncComponent asyncComponent;

    /**
     * 上传文件
     *
     * @param systemId
     * @param name
     * @param file
     * @return
     */
    public FileInfo upload(String systemId, String name, MultipartFile file) {
        SystemRegister systemRegister = systemRegisterCache.get(systemId);
        final ZlpUser user = CommonWebUtil.getUser();
        String userLoginId = Objects.isNull(user) ? "zlp" : user.getLoginId();
        String path = minioHelper.upload(systemRegister.getMinioBucket(), userLoginId, file);
        FileTypeEnum type = FileUtil.getFileType(file.getContentType());
        asyncComponent.uploadPreview(file, systemRegister, userLoginId, type);

        // 插入file数据
        LocalDateTime now = LocalDateTime.now();
        final FileInfo fileInfo = FileInfo.builder()
                .name(StringUtils.isNotBlank(name) ? name : FilenameUtils.getName(file.getOriginalFilename()))
                .path(path)
                .size(file.getSize())
                .suffix(file.getContentType())
                .type(type.getCode())
                .ownerId(Objects.isNull(user) ? null : user.getId())
                .uploaderIp(RequestUtil.getIp())
                .systemId(systemId)
                .ext(FileUtil.getMeta(file))
                .valid(ValidEnum.VALID.getCode())
                .createTime(now)
                .updateTime(now)
                .build();
        fileInfoExtendMapper.insertForId(fileInfo);
        return fileInfo;
    }

    /**
     * 获取文件对象
     *
     * @param systemId
     * @param fileId
     * @return
     */
    public FileInfo getFileInfo(String systemId, Long fileId) {
        return fileInfoExtendMapper.selectOne(FileInfo.builder().systemId(systemId).id(fileId).build());
    }

    /**
     * 获取文件
     *
     * @param systemId
     * @param filePath
     * @return
     */
    public InputStream getFileStream(String systemId, String filePath) {
        SystemRegister systemRegister = systemRegisterCache.get(systemId);
        return minioHelper.getStream(systemRegister.getMinioBucket(), filePath);
    }

    /**
     * 删除文件
     *
     * @param fileParam
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(FileParam fileParam) {
        SystemRegister systemRegister = systemRegisterCache.get(fileParam.getSystemId());
        if (!Objects.isNull(fileParam.getFileId())) {
            fileParam.getFileIds().add(fileParam.getFileId());
        }
        final List<FileInfo> fileInfos = fileInfoExtendMapper.batchSelect(fileParam.getFileIds());
        final List<String> paths = fileInfos.stream().map(FileInfo::getPath).collect(Collectors.toList());

        // 删除文件
        minioHelper.delete(systemRegister.getMinioBucket(), paths);
        // 将文件置为无效
        fileInfos.replaceAll(fileInfo -> {
            fileInfo.setValid(ValidEnum.INVALID.getCode());
            fileInfo.setUpdateTime(LocalDateTime.now());
            return fileInfo;
        });
        fileInfoExtendMapper.batchUpdate(fileInfos);
    }
}
