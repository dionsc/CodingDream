package com.dionst.service.service.impl;

import com.dionst.service.model.entity.JudgeRequest;
import com.dionst.service.mapper.JudgeRequestMapper;
import com.dionst.service.service.IJudgeRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 判题请求 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class JudgeRequestServiceImpl extends ServiceImpl<JudgeRequestMapper, JudgeRequest> implements IJudgeRequestService {

}
