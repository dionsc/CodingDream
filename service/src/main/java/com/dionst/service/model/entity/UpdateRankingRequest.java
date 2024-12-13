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
 * 榜单更新请求
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("update_ranking_request")
@ApiModel(value="UpdateRankingRequest对象", description="榜单更新请求")
public class UpdateRankingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提交的比赛")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "是否已经发送到队列")
    @TableField("updated")
    private Boolean updated;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_delete")
    @TableLogic
    private Integer isDelete;
}
