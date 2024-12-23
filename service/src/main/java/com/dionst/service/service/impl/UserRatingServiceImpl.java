package com.dionst.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.common.PageResult;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.mapper.*;
import com.dionst.service.model.dto.rating.UserRatingPageRequest;
import com.dionst.service.model.entity.*;
import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.model.vo.UserRatingVo;
import com.dionst.service.service.IUserRatingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户rating积分 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class UserRatingServiceImpl extends ServiceImpl<UserRatingMapper, UserRating> implements IUserRatingService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public PageResult getUserRatingByUserId(UserRatingPageRequest userRatingPageRequest) {
        QueryWrapper<UserRating> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(UserRating::getRatingChange, Integer.MIN_VALUE)
                .eq(UserRating::getUserId, userRatingPageRequest.getUserId());
        Page<UserRating> page = page(new Page<>(userRatingPageRequest.getCurrent(), userRatingPageRequest.getPageSize()), queryWrapper);
        List<UserRatingVo> records = new ArrayList<>();
        for (UserRating item : page.getRecords()) {
            UserRatingVo userRatingVo = new UserRatingVo();

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.lambda().select(User::getNickname).eq(User::getId, item.getUserId());
            userRatingVo.setUserName(userMapper.selectOne(userQueryWrapper).getNickname());

            QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
            contestQueryWrapper.lambda().select(Contest::getTitle).eq(Contest::getId, item.getContestId());
            userRatingVo.setContestTitle(contestMapper.selectOne(contestQueryWrapper).getTitle());

            userRatingVo.setRatingChange(item.getRatingChange());

            records.add(userRatingVo);
        }

        return new PageResult(page.getTotal(), records);
    }

    @Override
    public Integer getUserRatingCountByUserId(Long userId) {
        QueryWrapper<UserRating> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRating::getUserId, userId);
        List<UserRating> userRatingByUserId = list(queryWrapper);
        int rating = 0;
        for (UserRating userRating : userRatingByUserId) {
            rating += userRating.getRatingChange() == Integer.MIN_VALUE ? 0 : userRating.getRatingChange();
        }
        return rating;
    }

    @Override
    public void calculateContestRating(Long contestId) {
        //检查比赛是否结束
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda()
                .eq(Contest::getId, contestId);
        Contest contest = contestMapper.selectOne(contestQueryWrapper);
        if (contest == null || contest.getStartTime().plusMinutes(contest.getDuration()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.CONTEST_IS_RUNNING);
        }

        //查询比赛所有用户
        QueryWrapper<UserRating> userRatingQueryWrapper = new QueryWrapper<>();
        userRatingQueryWrapper.lambda()
                .select(UserRating::getUserId)
                .eq(UserRating::getContestId, contestId);
        List<UserRating> userRatingByUserId = list(userRatingQueryWrapper);

        //查询比赛总题数
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda()
                .eq(Question::getTitle, contestId);
        Long questionNum = questionMapper.selectCount(questionQueryWrapper);


        for (UserRating userRating : userRatingByUserId) {
            Long userId = userRating.getUserId();
            QueryWrapper<Submission> submissionQueryWrapper = new QueryWrapper<>();
            submissionQueryWrapper.lambda()
                    .eq(Submission::getUserId, userId)
                    .eq(Submission::getContestId, contestId)
                    .eq(Submission::getVerdict, VerdictEnum.Accepted.getValue())
                    .le(Submission::getSubmitTime, contest.getStartTime().plusMinutes(contest.getDuration()));
            HashSet<Long> s = new HashSet<>();
            submissionMapper.selectList(submissionQueryWrapper).forEach(submission -> {
                s.add(submission.getQuestionIndex());
            });

            //rating计算公式为：原rating + (100 * (过题数)/(比赛总题数))向上取整
            userRating.setRatingChange(100 * (int) ((s.size() + questionNum - 1) / questionNum));
            //更新
            updateById(userRating);
        }
    }


    //每天凌晨更新rating
    @Scheduled(cron = "0 0 0 * * ? ")
    public void updateUserRating() {

        QueryWrapper<ContestRatingUpdate> contestRatingUpdateQueryWrapper = new QueryWrapper<>();
        contestRatingUpdateQueryWrapper.lambda()
                .le(ContestRatingUpdate::getFinishTime, LocalDateTime.now());


    }
}
