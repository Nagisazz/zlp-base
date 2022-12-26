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

    /**
     * 上传文件
     *
     * @param bucketName
     * @param objectPath
     * @param inputStream
     */
    public void upload(String bucketName, String objectPath, InputStream inputStream) {
        try {
            minioClient.putObject(bucketName, objectPath, inputStream, inputStream.available(),
                    "application/octet-stream");
        } catch (Exception e) {
            log.error("minio上传文件失败，bucketName：{}，objectPath：{}", bucketName, objectPath);
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName
     * @param userLoginId
     * @param file
     * @return
     */
    public String upload(String bucketName, String userLoginId, MultipartFile file) {
        String path = getNowPath(userLoginId) + file.getOriginalFilename();
        try {
            upload(bucketName, path, file.getInputStream());
        } catch (Exception e) {
            log.error("minio上传文件失败，bucketName：{}，objectPath：{}", bucketName, path);
        }
        return path;
    }

    /**
     * 获取文件
     *
     * @param bucketName
     * @param objectPath
     * @return
     */
    public InputStream getStream(String bucketName, String objectPath) {
        try {
            return minioClient.getObject(bucketName, objectPath);
        } catch (Exception e) {
            log.error("minio下载文件失败，bucketName：{}，objectPath：{}", bucketName, objectPath);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectPath
     */
    public void delete(String bucketName, String objectPath) {
        try {
            minioClient.removeObject(bucketName, objectPath);
        } catch (Exception e) {
            log.error("minio删除文件失败，bucketName：{}，objectPath：{}", bucketName, objectPath);
        }
    }

    /**
     * 批量删除文件
     *
     * @param bucketName
     * @param objectPath
     */
    public void delete(String bucketName, List<String> objectPath) {
        try {
            minioClient.removeObject(bucketName, objectPath);
        } catch (Exception e) {
            log.error("minio批量删除文件失败，bucketName：{}，objectPath：{}", bucketName, objectPath);
        }
    }

    private String getNowPath(String userId) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DATE) + "/" + userId + "/";
    }
}
