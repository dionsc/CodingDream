package com.dionst.service.mapper;

import com.dionst.service.model.entity.Submission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 赛时提交 Mapper 接口
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {

}
