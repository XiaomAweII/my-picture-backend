package com.maxiaowei.yupicturebackend.controller;

import com.maxiaowei.yupicturebackend.common.BaseResponse;
import com.maxiaowei.yupicturebackend.common.ResultUtils;
import com.maxiaowei.yupicturebackend.common.annotation.AuthCheck;
import com.maxiaowei.yupicturebackend.common.constant.UserConstant;
import com.maxiaowei.yupicturebackend.model.dto.picture.PictureUploadRequest;
import com.maxiaowei.yupicturebackend.model.pojo.User;
import com.maxiaowei.yupicturebackend.model.vo.PictureVO;
import com.maxiaowei.yupicturebackend.service.PictureService;
import com.maxiaowei.yupicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片上传接口
 */
@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {
    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    /**
     * 上传图片（可重新上传）
     *
     * @param multipartFile
     * @param pictureUploadRequest
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("/file")MultipartFile multipartFile,
                                                 PictureUploadRequest pictureUploadRequest,
                                                 HttpServletRequest httpServletRequest) {
        User loginUser = userService.getLoginUser(httpServletRequest);
        PictureVO result = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(result);
    }
}
