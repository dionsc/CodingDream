package com.dionst.service.service;

import com.dionst.service.model.dto.question.QuestionAddRequest;
import com.dionst.service.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dionst.service.model.vo.QuestionVo;

import java.util.List;

/**
 * <p>
 * 比赛题目 服务类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
public interface IQuestionService extends IService<Question> {

    /**
     * 添加题目
     *
     * @param questionAddRequest
     * @return
     */
    Long addQuestion(QuestionAddRequest questionAddRequest);

    /**
     * 查看比赛题目
     *
     * @param contestId
     * @return
     */
    List<QuestionVo> getContestAllQuestion(Long contestId);

    /**
     * 查看题目详情
     * @param contestId
     * @param questionIndex
     * @return
     */
    QuestionVo getQuestion(Long contestId, Long questionIndex);
}
