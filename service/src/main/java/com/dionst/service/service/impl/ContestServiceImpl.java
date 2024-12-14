package com.dionst.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.common.PageResult;
import com.dionst.service.constant.ContestConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.dto.contest.ContestPageRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.mapper.ContestMapper;
import com.dionst.service.model.entity.User;
import com.dionst.service.model.entity.UserRating;
import com.dionst.service.service.IContestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.service.IUserRatingService;
import com.dionst.service.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 比赛 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements IContestService {

    @Autowired
    private IUserRatingService userRatingService;

    @Override
    public long addContest(ContestAddRequest contestAddRequest) {
        String description = contestAddRequest.getDescription();
        String title = contestAddRequest.getTitle();
        LocalDateTime startTime = contestAddRequest.getStartTime();
        Long duration = contestAddRequest.getDuration();

        //检查时间是否距离比赛开始时间过短
        if (startTime.isBefore(LocalDateTime.now().plusDays(ContestConstant.CREATE_BEFORE_START))) {
            throw new BusinessException(ErrorCode.CREATE_CLOSE_TO_START_TIME);
        }
        //检查比赛时长是否过短
        if (duration < ContestConstant.MIN_CONTEST_DURATION) {
            throw new BusinessException(ErrorCode.CONTEST_DURATION_TOO_SHORT);
        }
        Contest contest = new Contest().setDescription(description).setTitle(title).setStartTime(startTime).setDuration(duration);

        User user = UserHolder.getUser();
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        contest.setCreateId(user.getId());
        //保存
        save(contest);
        return contest.getCreateId();
    }

    @Override
    public PageResult pageContest(ContestPageRequest contestPageRequest) {
        int current = contestPageRequest.getCurrent();
        int pageSize = contestPageRequest.getPageSize();
        String title = contestPageRequest.getTitle();
        boolean asc = contestPageRequest.isAsc();

        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(
                        Contest::getId,
                        Contest::getTitle,
                        Contest::getDescription,
                        Contest::getDuration
                );
        if (!StrUtil.isBlank(title)) {
            queryWrapper.lambda()
                    .like(Contest::getTitle, title);
        }
        if (asc)
            queryWrapper.lambda()
                    .orderByAsc(Contest::getStartTime);
        else
            queryWrapper.lambda()
                    .orderByDesc(Contest::getStartTime);


        //分页查询
        Page<Contest> page = page(new Page<>(current, pageSize), queryWrapper);


        return new PageResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void participate(Long contestId) {
        //查看比赛是否已经结束
        Contest contest = getById(contestId);
        LocalDateTime startTime = contest.getStartTime();
        Long duration = contest.getDuration();
        if (startTime.plusMinutes(duration).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.CONTEST_FINISH);
        }
        //检查是否已经参赛
        QueryWrapper<UserRating> userRatingQueryWrapper = new QueryWrapper<>();
        userRatingQueryWrapper.lambda()
                .eq(UserRating::getContestId, contestId)
                .eq(UserRating::getUserId, UserHolder.getUser().getId());

        long c = userRatingService.count(userRatingQueryWrapper);
        //如果已经参赛
        if (c > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //保存
        UserRating userRating = new UserRating();
        userRating.setContestId(contestId);
        userRating.setUserId(UserHolder.getUser().getId());
        userRating.setRatingChange(Integer.MIN_VALUE);
        userRatingService.save(userRating);
    }
}
