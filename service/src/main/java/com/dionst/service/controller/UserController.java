package com.dionst.service.controller;


import com.dionst.service.common.Result;
import com.dionst.service.model.dto.LoginFormDTO;
import com.dionst.service.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation("请求登录")
    @PostMapping("/login")
    public Result<String> userLogin(@RequestBody LoginFormDTO loginFormDTO) {
        String token = userService.login(loginFormDTO);
        return Result.ok(token);
    }

    @ApiOperation("发送登录验证码")
    @PostMapping("/code/{phone}")
    public Result<String> sendCode(@PathVariable String phone) {
        log.info("请求验证码，手机号：{}", phone);
        userService.sendCode(phone);
        return Result.ok();
    }

}
