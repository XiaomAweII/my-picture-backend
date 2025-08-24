package com.maxiaowei.yupicturebackend.controller;

import com.maxiaowei.yupicturebackend.common.BaseResponse;
import com.maxiaowei.yupicturebackend.common.ResultUtils;
import com.maxiaowei.yupicturebackend.exception.ErrorCode;
import com.maxiaowei.yupicturebackend.exception.ThrowUtils;
import com.maxiaowei.yupicturebackend.model.dto.user.UserLoginRequest;
import com.maxiaowei.yupicturebackend.model.dto.user.UserRegisterRequest;
import com.maxiaowei.yupicturebackend.model.pojo.User;
import com.maxiaowei.yupicturebackend.model.vo.LoginUserVO;
import com.maxiaowei.yupicturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户功能接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(null == userRegisterRequest, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest,
                                               HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(null == userLoginRequest, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO result = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(null == httpServletRequest, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpServletRequest);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(null == httpServletRequest, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(httpServletRequest);
        return ResultUtils.success(result);
    }
}
