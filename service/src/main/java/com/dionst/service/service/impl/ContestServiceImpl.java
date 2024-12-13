package com.dionst.service.service.impl;

import com.dionst.service.common.ErrorCode;
import com.dionst.service.constant.ContestConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.entity.Contest;
import com.dionst.service.mapper.ContestMapper;
import com.dionst.service.model.entity.User;
import com.dionst.service.service.IContestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.utils.UserHolder;
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


    @Override
    public long addContest(ContestAddRequest contestAddRequest) {
        String description = contestAddRequest.getDescription();
        String title = contestAddRequest.getTitle();
        LocalDateTime startTime = contestAddRequest.getStartTime();
        Long duration = contestAddRequest.getDuration();

        //检查时间是否距离比赛开始时间过短
        if (startTime.isBefore(LocalDateTime.now().plusDays(ContestConstant.CREATE_BEFORE_START))) {
            throw new BusinessException(ErrorCode.CLOSE_TO_START_TIME);
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
}
