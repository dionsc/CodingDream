package com.dionst.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.model.entity.UpdateRankingRequest;
import com.dionst.service.mapper.UpdateRankingRequestMapper;
import com.dionst.service.model.entity.UserRating;
import com.dionst.service.service.IUpdateRankingRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.service.IUserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 榜单更新请求 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class UpdateRankingRequestServiceImpl extends ServiceImpl<UpdateRankingRequestMapper, UpdateRankingRequest> implements IUpdateRankingRequestService {

    @Autowired
    private IUserRatingService userRatingService;

    @Override
    @Transactional
    public void sendUpdateRankingRequest(Long contestId, Long userId) {
        QueryWrapper<UserRating> userRatingQueryWrapper = new QueryWrapper<>();
        userRatingQueryWrapper.lambda()
                .eq(UserRating::getContestId, contestId)
                .eq(UserRating::getUserId, userId);
        UserRating one = userRatingService.getOne(userRatingQueryWrapper);
        UpdateRankingRequest updateRankingRequest = new UpdateRankingRequest();
        updateRankingRequest.setContestId(contestId);
        updateRankingRequest.setUserId(userId);
        save(updateRankingRequest);



    }
}
