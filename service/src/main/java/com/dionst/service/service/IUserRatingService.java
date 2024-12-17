package com.dionst.service.service;

import com.dionst.service.model.entity.UserRating;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     * @param userId
     * @return
     */
    List<UserRating> getUserRatingByUserId(Long userId);

    /**
     * 查看用户累计积分
     * @param userId
     * @return
     */
    Integer getUserRatingCountByUserId(Long userId);
}
