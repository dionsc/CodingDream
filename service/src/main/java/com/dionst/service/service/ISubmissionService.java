package com.dionst.service.service;

import com.dionst.service.model.dto.submission.SubmissionAddRequest;
import com.dionst.service.model.entity.Submission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 赛时提交 服务类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
public interface ISubmissionService extends IService<Submission> {

    /**
     * 新增提交
     * @param submissionAddRequest
     */
    void add(SubmissionAddRequest submissionAddRequest);
}
