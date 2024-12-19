package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.question.QuestionAddRequest;
import com.dionst.service.model.entity.Question;
import com.dionst.service.model.vo.QuestionVo;
import com.dionst.service.service.IQuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 比赛题目 前端控制器
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @ApiOperation("添加题目")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN)
    public Result<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        Long questionId = questionService.addQuestion(questionAddRequest);
        return Result.ok(questionId);
    }

    @ApiOperation("查看比赛题目")
    @PostMapping("/all/{contestId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<List<QuestionVo>> getContestAllQuestion(@PathVariable Long contestId) {
        List<QuestionVo> result = questionService.getContestAllQuestion(contestId);
        return Result.ok(result);
    }

    @ApiOperation("查看题目详情")
    @PostMapping("/get/{questionId}")
    public Result<QuestionVo> getQuestion(@PathVariable Long questionId) {
        QuestionVo result = questionService.getQuestion(questionId);
        return Result.ok(result);
    }
}
