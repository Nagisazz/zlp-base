package com.nagisazz.platform.controller;

import com.google.common.base.Preconditions;
import com.nagisazz.base.entity.FileInfo;
import com.nagisazz.base.pojo.OperationResult;
import com.nagisazz.platform.enums.FileTypeEnum;
import com.nagisazz.platform.pojo.dto.FileParam;
import com.nagisazz.platform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("file")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 上传文件
     *
     * @param systemId
     * @param name
     * @param file
     * @return
     */
    @PostMapping("upload")
    @ResponseBody
    public OperationResult upload(@RequestParam("systemId") String systemId, @RequestParam("name") String name,
                                  @RequestParam("file") MultipartFile file) {
        Preconditions.checkArgument(StringUtils.isNotBlank(systemId), "系统标识为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(name), "文件名称为空");
        Preconditions.checkArgument(!Objects.isNull(file), "文件为空");
        return OperationResult.buildSuccessResult(fileService.upload(systemId, name, file));
    }

    /**
     * 获取文件详细信息
     *
     * @param systemId
     * @param fileId
     * @return
     */
    @GetMapping("get")
    @ResponseBody
    public OperationResult get(@RequestParam("systemId") String systemId, @RequestParam("fileId") Long fileId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(systemId), "系统标识为空");
        Preconditions.checkArgument(!Objects.isNull(fileId), "文件编号为空");
        return OperationResult.buildSuccessResult(fileService.getFileInfo(systemId, fileId));
    }

    /**
     * 获取文件流
     *
     * @param systemId
     * @param fileId
     * @return
     */
    @GetMapping("stream")
    public void stream(@RequestParam("systemId") String systemId, @RequestParam("fileId") Long fileId, HttpServletResponse response) {
        Preconditions.checkArgument(StringUtils.isNotBlank(systemId), "系统标识为空");
        Preconditions.checkArgument(!Objects.isNull(fileId), "文件为空");
        FileInfo fileInfo = fileService.getFileInfo(systemId, fileId);
        try (InputStream inputStream = fileService.getFileStream(systemId, fileInfo.getPath());
             OutputStream outputStream = response.getOutputStream()) {
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileInfo.getName(), StandardCharsets.UTF_8.name()));
            response.setContentType("application/octet-stream");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.info("获取文件流失败", e);
        }
    }

    /**
     * 预览缩略图
     *
     * @param systemId
     * @param fileId
     * @return
     */
    @GetMapping("preview")
    public void preview(@RequestParam("systemId") String systemId, @RequestParam("fileId") Long fileId, HttpServletResponse response) {
        Preconditions.checkArgument(StringUtils.isNotBlank(systemId), "系统标识为空");
        Preconditions.checkArgument(!Objects.isNull(fileId), "文件为空");
        FileInfo fileInfo = fileService.getFileInfo(systemId, fileId);
        Preconditions.checkArgument(!Objects.isNull(fileInfo), "文件为空");
        String path = fileInfo.getPath();
        String previewPath = path.replace(path.substring(path.lastIndexOf(".")), "_preview") + path.substring(path.lastIndexOf("."));
        try (InputStream inputStream = fileService.getFileStream(systemId, previewPath);
             OutputStream outputStream = response.getOutputStream()) {
            // 容错，当缩略图还未生成时临时生成
            if (Objects.isNull(inputStream)) {
                // 仅图片类型进行处理
                if (fileInfo.getType().equals(FileTypeEnum.IMAGE.getCode())) {
                    Thumbnails.of(fileService.getFileStream(systemId, fileInfo.getPath()))
                            .scale(0.25).toOutputStream(outputStream);
                }
            } else {
                IOUtils.copy(inputStream, outputStream);
            }
            response.setContentType("image/jpeg");
            outputStream.flush();
        } catch (Exception e) {
            log.info("获取文件流失败", e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileParam
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public OperationResult delete(@RequestBody FileParam fileParam) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileParam.getSystemId()), "系统标识为空");
        Preconditions.checkArgument(!Objects.isNull(fileParam.getFileId()) ||
                CollectionUtils.isNotEmpty(fileParam.getFileIds()), "文件编号为空");
        fileService.delete(fileParam);
        return OperationResult.buildSuccessResult("删除成功");
    }
}
