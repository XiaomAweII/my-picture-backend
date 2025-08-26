package com.maxiaowei.yupicturebackend.manager;

import com.maxiaowei.yupicturebackend.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 通用的对象存储操作
 */
@Slf4j
@Component
public class CosManager {
    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     *
     * @param key  唯一键
     * @param file 文件
     * @return
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectResult = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectResult);
    }

    /**
     * 下载对象
     *
     * @param key 唯一键
     * @return
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }

    /**
     * 上传对象（附带图片信息）
     * 需要开启数据万象 https://console.cloud.tencent.com/ci
     *
     * @param key  唯一键
     * @param file 文件
     */
    public PutObjectResult putPictureObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        // 对图片进行处理（获取基本信息也被视作为一种处理）
        PicOperations picOperations = new PicOperations();
        // 1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        // 构造处理参数
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }


    /**
     * 上传文件(还没有测试过)
     * 不对文件进行额外处理，通过流的方式将请求中的文件上传到COS，提高性能
     *
     * @param multipartFile 文件
     * @param key           唯一键
     * @return
     */
    public String uploadToCOS(MultipartFile multipartFile, String key) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // 元信息配置
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, inputStream, metadata);

            // 上传文件
            cosClient.putObject(putObjectRequest);

            // 生成访问链接
            return "https://" + cosClientConfig.getBucket() + ".cos." +
                    "." + cosClient.getClientConfig().getRegion().getRegionName() + ".myqcloud.com/" + key;
        } catch (Exception e) {
            log.error("CosManager uploadToCOS error", e);
        }
        return null;
    }
}
