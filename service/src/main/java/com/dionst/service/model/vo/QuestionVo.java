package com.dionst.service.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 比赛题目
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class QuestionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "题目所属比赛id")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "题目在本场比赛的编号(用户自己设置)")
    private Long questionIndex;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "题目描述")
    private String description;

    @ApiModelProperty(value = "判题时间限制(ms)")
    private Long timeLimit;

    @ApiModelProperty(value = "判题内存限制(MB)")
    private Long memoryLimit;

    @ApiModelProperty(value = "题目提交数量")
    private Long tryNumber;

    @ApiModelProperty(value = "题目通过数量")
    private Long acceptedNumber;

}
