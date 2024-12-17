package com.dionst.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dionst.service.model.entity.UserRating;
import com.dionst.service.mapper.UserRatingMapper;
import com.dionst.service.service.IUserRatingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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


    @Override
    public List<UserRating> getUserRatingByUserId(Long userId) {
        QueryWrapper<UserRating> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRating::getUserId, userId);
        return list(queryWrapper);
    }

    @Override
    public Integer getUserRatingCountByUserId(Long userId) {
        List<UserRating> userRatingByUserId = getUserRatingByUserId(userId);
        int rating = 0;
        for (UserRating userRating : userRatingByUserId) {
            rating += userRating.getRatingChange() == Integer.MIN_VALUE ? 0 : userRating.getRatingChange();
        }
        return rating;
    }


//    //每天凌晨更新rating
//    @Scheduled(cron = "0 0 0 * * ? ")
//    public void updateUserRating() {
//
//    }
}
