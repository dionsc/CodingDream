package com.dionst.service.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 比赛题目
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
@ApiModel(value="Question对象", description="比赛题目")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建者")
    @TableField("create_id")
    private Long createId;

    @ApiModelProperty(value = "题目所属比赛id")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "题目在本场比赛的编号(用户自己设置)")
    @TableField("question_index")
    private Long questionIndex;

    @ApiModelProperty(value = "题目标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "题目描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "判题时间限制(ms)")
    @TableField("time_limit")
    private Long timeLimit;

    @ApiModelProperty(value = "判题内存限制(MB)")
    @TableField("memory_limit")
    private Long memoryLimit;

    @ApiModelProperty(value = "题目提交数量")
    @TableField("try_number")
    private Long tryNumber;

    @ApiModelProperty(value = "题目通过数量")
    @TableField("accepted_number")
    private Long acceptedNumber;

    @ApiModelProperty(value = "判题程序")
    @TableField("judge_program_id")
    private Long judgeProgramId;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;
}
