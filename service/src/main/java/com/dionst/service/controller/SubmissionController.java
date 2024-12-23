package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.common.PageResult;
import com.dionst.service.common.Result;
import com.dionst.service.constant.RedisConstant;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.model.dto.submission.SubmissionAddRequest;
import com.dionst.service.model.dto.submission.SubmissionPageRequest;
import com.dionst.service.model.entity.Submission;
import com.dionst.service.service.ISubmissionService;
import com.dionst.service.utils.UserHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 赛时提交 前端控制器
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@RestController
@RequestMapping("/submission")
public class SubmissionController {


    @Autowired
    private ISubmissionService submissionService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/add")
    @ApiOperation("新增提交")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<String> add(@RequestBody SubmissionAddRequest submissionAddRequest) {

        //防止短时多次提交
        String key = RedisConstant.SUBMIT_CHECK + UserHolder.getUser().getId();
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", RedisConstant.SUBMIT_LIMIT, TimeUnit.SECONDS);

        if (!Boolean.TRUE.equals(success)) {
            throw new BusinessException(ErrorCode.FREQUENT_SUBMIT);
        }

        submissionService.add(submissionAddRequest);
        return Result.ok();
    }

    @PostMapping("/page")
    @ApiOperation("查看提交")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<PageResult> pageSearch(@RequestBody SubmissionPageRequest submissionPageRequest) {
        PageResult result = submissionService.pageSearch(submissionPageRequest);
        return Result.ok(result);
    }

}
