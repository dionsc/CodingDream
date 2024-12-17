package com.dionst.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.mapper.*;
import com.dionst.service.model.dto.program.ProgramAddRequest;
import com.dionst.service.model.dto.submission.SubmissionAddRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.model.entity.Program;
import com.dionst.service.model.entity.Submission;
import com.dionst.service.model.entity.UserRating;
import com.dionst.service.model.enums.ProgramLanguageEnum;
import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 赛时提交 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements ISubmissionService {

    @Autowired
    private UserRatingMapper userRatingMapper;

    @Autowired
    private ProgramMapper programMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private IJudgeRequestService judgeRequestService;

    @Override
    @Transactional
    public void add(SubmissionAddRequest submissionAddRequest) {

        LocalDateTime submitTime = LocalDateTime.now();

        Long contestId = submissionAddRequest.getContestId();
        Long questionIndex = submissionAddRequest.getQuestionIndex();
        ProgramAddRequest code = submissionAddRequest.getCode();
        //查看当前用户是否参赛
        QueryWrapper<UserRating> userRatingQueryWrapper = new QueryWrapper<>();
        userRatingQueryWrapper.lambda()
                .eq(UserRating::getContestId, contestId)
                .eq(UserRating::getUserId, UserHolder.getUser().getId());
        long c = userRatingMapper.selectCount(userRatingQueryWrapper);
        //如果为参赛
        if (c == 0) {
            throw new BusinessException(ErrorCode.NO_PARTICIPATION);
        }
        //检查代码编程语言是否支持
        ProgramLanguageEnum enumByValue = ProgramLanguageEnum.getEnumByValue(code.getLanguage());
        if (enumByValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //检查比赛是否正在进行
        Contest contest = contestMapper.selectById(contestId);
        LocalDateTime startTime = contest.getStartTime();
        Long duration = contest.getDuration();
        if (submitTime.isAfter(startTime.plusMinutes(duration))
                || submitTime.isBefore(startTime)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }


        //保存代码
        Program program = new Program();
        program.setCode(code.getCode());
        program.setLanguage(code.getLanguage());
        programMapper.insert(program);

        //保存提交
        Submission submission = new Submission();
        submission.setContestId(contestId);
        submission.setQuestionIndex(questionIndex);
        submission.setCodeId(program.getId());
        submission.setUserId(UserHolder.getUser().getId());
        submission.setCodeId(program.getId());
        submission.setVerdict(VerdictEnum.Pending.getValue());
        submission.setSubmitTime(submitTime);
        save(submission);

        //发送判题请求
        judgeRequestService.sendJudgeRequest(submission.getId());
    }
}
