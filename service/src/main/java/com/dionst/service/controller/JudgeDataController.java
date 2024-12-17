package com.dionst.service.controller;


import com.dionst.service.annotation.AuthCheck;
import com.dionst.service.common.Result;
import com.dionst.service.constant.UserConstant;
import com.dionst.service.model.dto.judgeData.JudgeDataAddRequest;
import com.dionst.service.model.entity.JudgeData;
import com.dionst.service.service.IJudgeDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 判题数据 前端控制器
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@RestController
@RequestMapping("/judge-data")
public class JudgeDataController {

    @Autowired
    private IJudgeDataService judgeDataService;

    @PostMapping("/add")
    @ApiOperation("添加判题数据")
    @AuthCheck(mustRole = UserConstant.ADMIN)
    public Result<String> add(@RequestBody JudgeDataAddRequest judgeDataAddRequest) {
        judgeDataService.add(judgeDataAddRequest);
        return Result.ok();
    }
}
