package com.dionst.service.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 赛时提交
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class SubmissionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户编号")
    private Long userId;

    @ApiModelProperty(value = "参加的比赛的编号")
    private Long contestId;

    @ApiModelProperty(value = "题目在本场比赛的编号(用户自己设置)")
    private Long questionIndex;

    @ApiModelProperty(value = "提交的代码")
    private Long codeId;

    @ApiModelProperty(value = "系统判定结果")
    private String verdict;

    @ApiModelProperty(value = "代码提交时间")
    private LocalDateTime submitTime;
    
}
