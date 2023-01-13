package com.nagisazz.base.util;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectPath)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType("application/octet-stream")
                    .build());
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
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectPath)
                    .build());
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
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectPath)
                    .build());
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
            List<DeleteObject> objects = Lists.newArrayList();
            objectPath.forEach(e -> objects.add(new DeleteObject(e)));
            Iterable<Result<DeleteError>> results =
                    minioClient.removeObjects(
                            RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("minio批量删除文件失败，bucketName：{}，DeleteError：{}", bucketName, error);
            }
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
