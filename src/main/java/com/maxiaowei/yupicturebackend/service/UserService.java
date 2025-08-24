package com.maxiaowei.yupicturebackend.service;

import com.maxiaowei.yupicturebackend.model.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-08-24 20:20:15
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 获取加密后的密码
     *
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);
}
