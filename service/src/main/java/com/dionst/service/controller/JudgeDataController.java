package com.dionst.service.controller;


import com.dionst.service.model.entity.JudgeData;
import com.dionst.service.service.IJudgeDataService;
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

    @PostMapping
    public String Add(@RequestBody JudgeData judgeData) {
        judgeDataService.save(judgeData);
        return "success";
    }

    @DeleteMapping
    private boolean Delete(Long id) {
        boolean b = judgeDataService.removeById(id);
        return b;
    }
}
