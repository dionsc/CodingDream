package com.dionst.service.service;

import com.dionst.service.common.PageResult;
import com.dionst.service.model.dto.rating.UserRatingPageRequest;
import com.dionst.service.model.entity.UserRating;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户rating积分 服务类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
public interface IUserRatingService extends IService<UserRating> {

    /**
     * 获取用户的Rating变化
     *
     * @param userRatingPageRequest
     * @return
     */
    PageResult getUserRatingByUserId(UserRatingPageRequest userRatingPageRequest);

    /**
     * 查看用户累计积分
     * @param userId
     * @return
     */
    Integer getUserRatingCountByUserId(Long userId);

    /**
     * 计算所有参赛用户的rating变化
     * @param contestId
     */
    void calculateContestRating(Long contestId);
}
