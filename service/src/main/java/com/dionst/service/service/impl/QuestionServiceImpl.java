package com.dionst.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.constant.ContestConstant;
import com.dionst.service.constant.QuestionConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.mapper.ContestMapper;
import com.dionst.service.mapper.ProgramMapper;
import com.dionst.service.model.dto.program.ProgramAddRequest;
import com.dionst.service.model.dto.question.QuestionAddRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.model.entity.Program;
import com.dionst.service.model.entity.Question;
import com.dionst.service.mapper.QuestionMapper;
import com.dionst.service.model.enums.ProgramLanguageEnum;
import com.dionst.service.model.vo.QuestionVo;
import com.dionst.service.service.IContestService;
import com.dionst.service.service.IProgramService;
import com.dionst.service.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.utils.UserHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 比赛题目 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private ProgramMapper programMapper;

    @Override
    @Transactional
    public Long addQuestion(QuestionAddRequest questionAddRequest) {
        Long contestId = questionAddRequest.getContestId();
        Long questionIndex = questionAddRequest.getQuestionIndex();
        String title = questionAddRequest.getTitle();
        String description = questionAddRequest.getDescription();
        Long timeLimit = questionAddRequest.getTimeLimit();
        Long memoryLimit = questionAddRequest.getMemoryLimit();
        ProgramAddRequest judgeProgram = questionAddRequest.getJudgeProgram();

        String language = judgeProgram.getLanguage();
        String code = judgeProgram.getCode();

        //查看比赛是否存在
        Contest contest = contestMapper.selectById(contestId);
        if (contest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //查看比赛是否距离比赛开始时间过近
        if (contest.getStartTime().isBefore(LocalDateTime.now().plusHours(ContestConstant.UPDATE_BEFORE_START))) {
            throw new BusinessException(ErrorCode.UPDATE_CLOSE_TO_START_TIME);
        }
        //检查时间限制是否在设置范围内
        if (timeLimit == null || timeLimit <= 0 || timeLimit > QuestionConstant.MAX_TIME_LIMIT)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //检查空间限制是否在设置范围内
        if (memoryLimit == null || memoryLimit <= 0 || memoryLimit > QuestionConstant.MAX_MEMORY_LIMIT)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //检查编程判题程序编程语言是否存在
        ProgramLanguageEnum enumByValue = ProgramLanguageEnum.getEnumByText(language);
        if (null == enumByValue) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        //保存代码
        Program program = new Program();
        BeanUtils.copyProperties(judgeProgram, program);
        programMapper.insert(program);
        //保存题目
        Question question = new Question();
        question.setJudgeProgramId(program.getId());
        question.setCreateId(UserHolder.getUser().getId());
        BeanUtils.copyProperties(questionAddRequest, question);
        save(question);
        return question.getId();
    }

    @Override
    public List<QuestionVo> getContestAllQuestion(Long contestId) {
        //检查比赛是否存在和比赛是否开始
        checkContestStart(contestId);

        //查询
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda()
                .select(
                        Question::getQuestionIndex,
                        Question::getId,
                        Question::getAcceptedNumber,
                        Question::getTryNumber,
                        Question::getTitle,
                        Question::getMemoryLimit,
                        Question::getTimeLimit)
                .eq(Question::getContestId, contestId);
        List<Question> questionList = list(questionQueryWrapper);
        List<QuestionVo> questionVoList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionVo questionVo = new QuestionVo();
            BeanUtils.copyProperties(question, questionVo);
            questionVoList.add(questionVo);
        }
        return questionVoList;
    }


    @Override
    public QuestionVo getQuestion(Long questionId) {

        Question question = getById(questionId);
        checkContestStart(question.getContestId());
        QuestionVo questionVo = new QuestionVo();
        BeanUtils.copyProperties(question, questionVo);
        return questionVo;
    }


    private void checkContestStart(Long contestId) {
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda()
                .select(Contest::getStartTime)
                .eq(Contest::getId, contestId);
        Contest contest = contestMapper.selectOne(contestQueryWrapper);
        //检查比赛是否存在
        if (contest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //检查是否已经到了比赛时间
        LocalDateTime startTime = contest.getStartTime();
        if (startTime.isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }
}
