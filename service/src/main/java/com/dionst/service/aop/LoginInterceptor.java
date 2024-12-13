package com.dionst.service.aop;

import cn.hutool.core.util.StrUtil;
import com.dionst.service.constant.RedisConstant;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.entity.User;
import com.dionst.service.utils.UserHolder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@Order(1)
public class LoginInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Gson gson = new Gson();

    @Around("execution(* com.dionst.service.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String token = httpServletRequest.getHeader(UserConstant.LOGIN_TOKEN);

        //获去用户信息
        if(!StrUtil.isBlank(token))
        {
            String key = RedisConstant.LOGIN_TOKEN_KEY + token;
            String userJSON = stringRedisTemplate.opsForValue().get(key);
            if (!StrUtil.isBlank(userJSON))
            {
                User user = gson.fromJson(userJSON, User.class);
                //保存用户信息到ThreadLocal
                UserHolder.saveUser(user);
                //刷新token有效期
                stringRedisTemplate.expire(key,RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
            }
        }

        // 执行原方法
        Object result = point.proceed();
        UserHolder.removeUser();

        return result;
    }
}
