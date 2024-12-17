package com.dionst.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.constant.ContestConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.mapper.ContestMapper;
import com.dionst.service.mapper.QuestionMapper;
import com.dionst.service.model.dto.judgeData.JudgeDataAddRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.model.entity.JudgeData;
import com.dionst.service.mapper.JudgeDataMapper;
import com.dionst.service.model.entity.Question;
import com.dionst.service.service.IContestService;
import com.dionst.service.service.IJudgeDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.service.IQuestionService;
import com.dionst.service.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 判题数据 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class JudgeDataServiceImpl extends ServiceImpl<JudgeDataMapper, JudgeData> implements IJudgeDataService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Override
    public void add(JudgeDataAddRequest judgeDataAddRequest) {
        String name = judgeDataAddRequest.getName();
        Long questionId = judgeDataAddRequest.getQuestionId();


        //检查对应题目是否存在
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda()
                .select(
                        Question::getContestId,
                        Question::getCreateId)
                .eq(Question::getId, questionId);
        Question question = questionMapper.selectOne(questionQueryWrapper);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //判断当前登录用户是否为题目的创建者
        if (!Objects.equals(question.getCreateId(), UserHolder.getUser().getId()))
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);

        //检查该题目所在比赛是否开始
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda()
                .select(Contest::getStartTime)
                .eq(Contest::getId, question.getContestId());
        Contest contest = contestMapper.selectOne(contestQueryWrapper);
        if (contest.getStartTime().isBefore(LocalDateTime.now().plusHours(ContestConstant.UPDATE_BEFORE_START)))
            throw new BusinessException(ErrorCode.UPDATE_CLOSE_TO_START_TIME);

        //检查数据是否命名
        if (StrUtil.isBlank(name))
            name = UUID.randomUUID().toString().replace("-", "").substring(0, 6);

        //保存
        JudgeData judgeData = new JudgeData();
        judgeData.setName(name);
        judgeData.setQuestionId(questionId);
        judgeData.setInput(judgeDataAddRequest.getInput());
        judgeData.setOutput(judgeDataAddRequest.getOutput());
        save(judgeData);
    }
}
