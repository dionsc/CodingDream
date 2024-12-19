package com.dionst.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.LoginFormDTO;
import com.dionst.service.model.entity.*;
import com.dionst.service.service.IUserService;
import com.dionst.service.utils.UserHolder;
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

    @ApiOperation("获取登录用户信息")
    @PostMapping("/get/login")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<User> getLoginUser() {
        User user = UserHolder.getUser();
        User result = new User();
        result.setId(user.getId());
        result.setUserRole(user.getUserRole());
        result.setNickname(user.getNickname());
        return Result.ok(result);
    }


    @PostMapping("/1")
    public String test1(@RequestBody Contest contest){
        return "ok";
    }
    @PostMapping("/2")
    public String test2(@RequestBody JudgeData contest){
        return "ok";
    }
    @PostMapping("/3")
    public String test1(@RequestBody Question contest){
        return "ok";
    }
    @PostMapping("/4")
    public String test4(@RequestBody Submission contest){
        return "ok";
    }
    @PostMapping("/5")
    public String test5(@RequestBody UserRating contest){
        return "ok";
    }








}
