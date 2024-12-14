package com.dionst.service.aop;

import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.model.entity.User;
import com.dionst.service.model.enums.UserRoleEnum;
import com.dionst.service.service.IUserService;
import com.dionst.service.utils.UserHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Aspect
@Component
@Order(2)
public class AuthInterceptor {

    @Resource
    private IUserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = UserHolder.getUser();
        Integer userRoleValue = loginUser == null ? null : loginUser.getUserRole();
        Integer mustRoleValue = UserRoleEnum.getEnumByText(mustRole);
        // 不需要权限，放行
        if (mustRoleValue == null) {
            return joinPoint.proceed();
        }
        // 必须有该权限才通过
        if (userRoleValue == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 如果被封号，直接拒绝
        if (UserRoleEnum.BAN.getValue() == userRoleValue) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 必须有管理员权限
        if (UserRoleEnum.ADMIN.getValue() == mustRoleValue) {
            // 用户没有管理员权限，拒绝
            if (UserRoleEnum.ADMIN.getValue() != userRoleValue) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

