package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.submission.SubmissionAddRequest;
import com.dionst.service.model.entity.Submission;
import com.dionst.service.service.ISubmissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/add")
    @ApiOperation("新增提交")
    @AuthCheck(mustRole = UserConstant.DEFAULT)
    public Result<String> add(@RequestBody SubmissionAddRequest submissionAddRequest) {
        submissionService.add(submissionAddRequest);
        return Result.ok();
    }

}
