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
 * 赛时提交
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("submission")
@ApiModel(value="Submission对象", description="赛时提交")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "参赛者队伍编号")
    @TableField("participant_id")
    private Long participantId;

    @ApiModelProperty(value = "参加的比赛的编号")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "题目在本场比赛的编号(用户自己设置)")
    @TableField("question_index")
    private Long questionIndex;

    @ApiModelProperty(value = "提交的代码")
    @TableField("code_id")
    private Long codeId;

    @ApiModelProperty(value = "系统判定结果")
    @TableField("verdict")
    private String verdict;

    @ApiModelProperty(value = "代码提交时间")
    @TableField("submit_time")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;
}
