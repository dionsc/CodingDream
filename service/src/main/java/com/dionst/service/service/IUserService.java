package com.dionst.service.service;

import com.dionst.service.model.dto.LoginFormDTO;
import com.dionst.service.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     * @param loginFormDTO
     * @return
     */
    String login(LoginFormDTO loginFormDTO);

    /**
     * 发送登录验证码
     *
     * @param phone
     */
    void sendCode(String phone);
}
