package com.maxiaowei.yupicturebackend.aop;

import cn.hutool.core.util.StrUtil;
import com.maxiaowei.yupicturebackend.common.annotation.AuthCheck;
import com.maxiaowei.yupicturebackend.common.exception.BusinessException;
import com.maxiaowei.yupicturebackend.common.exception.ErrorCode;
import com.maxiaowei.yupicturebackend.model.enums.UserRoleEnum;
import com.maxiaowei.yupicturebackend.model.pojo.User;
import com.maxiaowei.yupicturebackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验切面
 */
@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        // 不需要角色控制
        if (StrUtil.isBlank(mustRole)) {
            return joinPoint.proceed();
        }

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User currentUser = userService.getLoginUser(request);

        // 匹配角色权限
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 不需要权限，放行
        if (null == mustRoleEnum) {
            return joinPoint.proceed();
        }

        // 以下为必须拥有该权限才通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(currentUser.getUserRole());
        // 用户无权限
        if (null == userRoleEnum) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 要求必须有管理员权限，但用户没有管理员权限，拒绝
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 权限校验通过，放行
        return joinPoint.proceed();
    }
}
