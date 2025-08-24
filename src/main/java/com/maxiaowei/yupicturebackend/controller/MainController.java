package com.maxiaowei.yupicturebackend.controller;

import com.maxiaowei.yupicturebackend.common.BaseResponse;
import com.maxiaowei.yupicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例接口 用于健康检查
 */
@RestController
@RequestMapping("/")
public class MainController {
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok!");
    }

}
