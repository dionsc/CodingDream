package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.PageResult;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.dto.contest.ContestPageRequest;
import com.dionst.service.model.dto.contest.SendMessageRequest;
import com.dionst.service.model.dto.ranking.RankingPageRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.service.IContestService;
import com.dionst.service.service.IUserRatingService;
import com.dionst.service.utils.CommonUtils;
import com.google.gson.Gson;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @Autowired
    private Gson gson;

    @Autowired
    private RabbitTemplate rabbitTemplate;


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

    @ApiOperation("获取比赛榜单")
    @PostMapping("/ranking")
    public Result<PageResult> getRanking(@RequestBody RankingPageRequest rankingPageRequest) {
        PageResult result = contestService.getRanking(rankingPageRequest);
        return Result.ok(result);
    }

    @ApiOperation("管理员向前端广播消息")
    @PostMapping("/send-message")
    @AuthCheck(mustRole = UserConstant.ADMIN)
    public Result<String> sendMessageToUser(@RequestBody SendMessageRequest sendMessageRequest) {
        CommonUtils.sendMessageToFrontend(gson.toJson(sendMessageRequest), rabbitTemplate);
        return Result.ok();
    }
}
