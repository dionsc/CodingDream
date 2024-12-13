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
 * 用户rating积分
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_rating")
@ApiModel(value="UserRating对象", description="用户rating积分")
public class UserRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "比赛编号")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "在本场比赛获得的积分")
    @TableField("rating_change")
    private Integer ratingChange;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;
}
