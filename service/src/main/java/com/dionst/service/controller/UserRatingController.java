package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.PageResult;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.rating.UserRatingPageRequest;
import com.dionst.service.model.entity.UserRating;
import com.dionst.service.model.vo.UserRatingVo;
import com.dionst.service.service.IUserRatingService;
import com.dionst.service.utils.UserHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/page")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<PageResult> getUserRatingByUserId(@RequestBody UserRatingPageRequest userRatingPageRequest) {
        userRatingPageRequest.setUserId(UserHolder.getUser().getId());
        PageResult result = userRatingService.getUserRatingByUserId(userRatingPageRequest);
        return Result.ok(result);
    }

    @ApiOperation("查看用户累计积分")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<Integer> getUserRatingCountByUserId() {
        Integer rating = userRatingService.getUserRatingCountByUserId(UserHolder.getUser().getId());
        return Result.ok(rating);
    }


    @PostMapping("/calculate-rating/{contestId}")
    @ApiOperation("计算所有参赛用户的rating变化")
    @AuthCheck(mustRole = UserConstant.ADMIN)
    public Result<String> calculateContestRating(@PathVariable Long contestId) {
        userRatingService.calculateContestRating(contestId);
        return Result.ok();
    }
}
