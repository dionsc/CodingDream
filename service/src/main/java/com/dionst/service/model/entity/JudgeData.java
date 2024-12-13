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
 * 判题数据
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("judge_data")
@ApiModel(value="JudgeData对象", description="判题数据")
public class JudgeData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "数据名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "数据对应题目")
    @TableField("question_id")
    private Long questionId;

    @ApiModelProperty(value = "数据部分")
    @TableField("data")
    private String data;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;

}
