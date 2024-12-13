package com.dionst.service.service.impl;

import com.dionst.service.model.entity.UserRating;
import com.dionst.service.mapper.UserRatingMapper;
import com.dionst.service.service.IUserRatingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
