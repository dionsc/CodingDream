package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.PageResult;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.dto.contest.ContestPageRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.service.IContestService;
import com.dionst.service.service.IUserRatingService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("分页查看比赛列表")
    @PostMapping("/page")
    public Result<PageResult> pageContest(@RequestBody ContestPageRequest contestPageRequest) {
        PageResult result = contestService.pageContest(contestPageRequest);
        return Result.ok(result);
    }

    @ApiOperation("参加比赛")
    @PostMapping("participate/{contestId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<String> participate(@PathVariable Long contestId) {
        contestService.participate(contestId);
        return Result.ok();
    }
}
