package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.entity.UserRating;
import com.dionst.service.service.IUserRatingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户rating积分 前端控制器
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@RestController
@RequestMapping("/user-rating")
public class UserRatingController {

    @Autowired
    private IUserRatingService userRatingService;

    @ApiOperation("获取用户的Rating变化")
    @PostMapping("/list/{userId}")
    public Result<List<UserRating>> getUserRatingByUserId(@PathVariable Long userId) {
        List<UserRating> result = userRatingService.getUserRatingByUserId(userId);
        return Result.ok(result);
    }

    @
    ApiOperation("查看用户累计积分")
    @PostMapping("/get/{userId}")
    public Result<Integer> getUserRatingCountByUserId(@PathVariable Long userId) {
        Integer rating = userRatingService.getUserRatingCountByUserId(userId);
        return Result.ok(rating);
    }
}
