package com.dionst.service.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.constant.RedisConstant;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.model.dto.LoginFormDTO;
import com.dionst.service.model.entity.User;
import com.dionst.service.mapper.UserMapper;
import com.dionst.service.model.enums.UserRoleEnum;
import com.dionst.service.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.utils.RegexUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Gson gson;

    @Override
    public String login(LoginFormDTO loginFormDTO) {
        log.info("用户登录phone:{},code:{}", loginFormDTO.getPhone(), loginFormDTO.getCode());
        String phone = loginFormDTO.getPhone();
        String code = loginFormDTO.getCode();
        //校验手机号码
        if (RegexUtils.isPhoneInvalid(phone)) {
            //如果不符合
            throw new BusinessException(ErrorCode.PHONE_PATTERN_ERROR);
        }
        //校验验证码
        String s = stringRedisTemplate.opsForValue().get(RedisConstant.LOGIN_CODE_KEY + phone);
        if (s == null || !s.equals(code)) {
            throw new BusinessException(ErrorCode.PHNOE_CODE_ERROE);
        }
        //根据手机号查询用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        User user = userMapper.selectOne(wrapper);

        //如果是新用户则则创建用户
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setUserRole(UserRoleEnum.USER.getValue());
            user.setNickname(UUID.randomUUID().toString().replace("-", ""));
            userMapper.insert(user);
        }
        //生成随机token
        String token = UUID.randomUUID().toString();

        //保存用户信息到redis中
        String key = RedisConstant.LOGIN_TOKEN_KEY + token;

        String userDTOJSON = gson.toJson(user);
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, userDTOJSON, RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.MINUTES))) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //返回token
        return token;
    }

    @Override
    public void sendCode(String phone) {

        //校验手机号码
        if (RegexUtils.isPhoneInvalid(phone)) {
            //如果不符合
            throw new BusinessException(ErrorCode.PHONE_PATTERN_ERROR);
        }
        //生成验证码
        String code = RandomUtil.randomNumbers(UserConstant.codeLength);

        //保存验证码到reids
        Boolean Boolean = stringRedisTemplate.opsForValue()
                .setIfAbsent(RedisConstant.LOGIN_CODE_KEY + phone, code, RedisConstant.LOGIN_CODE_TTL, TimeUnit.MINUTES);

        if (java.lang.Boolean.FALSE.equals(Boolean)) {
            throw new BusinessException(ErrorCode.FREQUENT_GET_CODE);
        }

        //发送验证码
        //todo 利用外部服务进行发送
        log.info("发送短信验证码，验证码：{}", code);
    }
}
