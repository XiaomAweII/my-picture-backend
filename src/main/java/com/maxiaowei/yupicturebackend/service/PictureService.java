package com.maxiaowei.yupicturebackend.service;

import com.maxiaowei.yupicturebackend.model.dto.picture.PictureUploadRequest;
import com.maxiaowei.yupicturebackend.model.pojo.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxiaowei.yupicturebackend.model.pojo.User;
import com.maxiaowei.yupicturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Administrator
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-08-26 20:50:58
*/
public interface PictureService extends IService<Picture> {
    /**
     * 上传图片
     *
     * @param multipartFile
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(MultipartFile multipartFile,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);
}
