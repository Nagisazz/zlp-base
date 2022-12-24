package com.nagisazz.base.util;

import com.nagisazz.base.property.MinioProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MinioHelper {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    /**
     * 上传文件
     *
     * @param objectPath
     * @param inputStream
     */
    public void upload(String objectPath, InputStream inputStream) {
        try {
            minioClient.putObject(minioProperties.getBucketName(), objectPath, inputStream, inputStream.available(),
                    "application/octet-stream");
        } catch (Exception e) {
            log.error("minio上传文件失败，bucketName：{}，objectPath：{}", minioProperties.getBucketName(), objectPath);
        }
    }

    /**
     * 上传文件
     *
     * @param userLoginId
     * @param file
     * @return
     */
    public String upload(String userLoginId, MultipartFile file) {
        String path = getNowPath(userLoginId) + file.getOriginalFilename();
        try {
            upload(path, file.getInputStream());
        } catch (Exception e) {
            log.error("minio上传文件失败，bucketName：{}，objectPath：{}", minioProperties.getBucketName(), path);
        }
        return path;
    }

    /**
     * 获取文件
     *
     * @param objectPath
     * @return
     */
    public InputStream getStream(String objectPath) {
        try {
            return minioClient.getObject(minioProperties.getBucketName(), objectPath);
        } catch (Exception e) {
            log.error("minio下载文件失败，bucketName：{}，objectPath：{}", minioProperties.getBucketName(), objectPath);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param objectPath
     */
    public void delete(String objectPath) {
        try {
            minioClient.removeObject(minioProperties.getBucketName(), objectPath);
        } catch (Exception e) {
            log.error("minio删除文件失败，bucketName：{}，objectPath：{}", minioProperties.getBucketName(), objectPath);
        }
    }

    /**
     * 批量删除文件
     *
     * @param objectPath
     */
    public void delete(List<String> objectPath) {
        try {
            minioClient.removeObject(minioProperties.getBucketName(), objectPath);
        } catch (Exception e) {
            log.error("minio批量删除文件失败，bucketName：{}，objectPath：{}", minioProperties.getBucketName(), objectPath);
        }
    }

    private String getNowPath(String userId) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DATE) + "/" + userId + "/";
    }
}
