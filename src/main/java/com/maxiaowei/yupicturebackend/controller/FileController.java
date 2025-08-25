package com.maxiaowei.yupicturebackend.controller;

import com.maxiaowei.yupicturebackend.common.BaseResponse;
import com.maxiaowei.yupicturebackend.common.ResultUtils;
import com.maxiaowei.yupicturebackend.common.annotation.AuthCheck;
import com.maxiaowei.yupicturebackend.common.constant.UserConstant;
import com.maxiaowei.yupicturebackend.common.exception.BusinessException;
import com.maxiaowei.yupicturebackend.common.exception.ErrorCode;
import com.maxiaowei.yupicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

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
}
