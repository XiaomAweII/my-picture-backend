package com.maxiaowei.yupicturebackend.controller;

import com.maxiaowei.yupicturebackend.common.BaseResponse;
import com.maxiaowei.yupicturebackend.common.ResultUtils;
import com.maxiaowei.yupicturebackend.common.annotation.AuthCheck;
import com.maxiaowei.yupicturebackend.common.constant.UserConstant;
import com.maxiaowei.yupicturebackend.common.exception.BusinessException;
import com.maxiaowei.yupicturebackend.common.exception.ErrorCode;
import com.maxiaowei.yupicturebackend.manager.CosManager;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private CosManager cosManager;

    /**
     * 测试文件上传
     *
     * @param multipartFile
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        // 文件目录
        String filename = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filePath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filePath, file);
            // 返回可访问地址
            return ResultUtils.success(filePath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (null != file) {
                // 删除历史文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = " + filePath);
                }
            }
        }
    }

    /**
     * 测试文件下载
     *
     * @param filePath
     * @param response
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download")
    public void testDownloadFile(String filePath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filePath);
            cosObjectInput = cosObject.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filePath);
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filePath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (null != cosObjectInput) {
                cosObjectInput.close();
            }
        }
    }
}
