package com.dionst.service.service;

import com.dionst.service.model.dto.judgeData.JudgeDataAddRequest;
import com.dionst.service.model.entity.JudgeData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 判题数据 服务类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
public interface IJudgeDataService extends IService<JudgeData> {

    /**
     * 添加判题数据
     * @param judgeDataAddRequest
     */
    void add(JudgeDataAddRequest judgeDataAddRequest);
}
