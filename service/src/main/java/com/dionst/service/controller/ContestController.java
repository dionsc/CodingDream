package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.service.IContestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 比赛 前端控制器
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private IContestService contestService;


    @ApiOperation("添加比赛")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN)
    public Result<Long> addContest(@RequestBody ContestAddRequest contestAddRequest) {
        long contestId = contestService.addContest(contestAddRequest);
        return Result.ok(contestId);
    }
}
