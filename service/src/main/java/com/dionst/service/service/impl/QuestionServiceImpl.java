package com.dionst.service.service.impl;

import com.dionst.service.model.entity.Question;
import com.dionst.service.mapper.QuestionMapper;
import com.dionst.service.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 比赛题目 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

}
