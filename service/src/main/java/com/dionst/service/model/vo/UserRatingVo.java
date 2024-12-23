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
@ApiModel(value = "UserRating对象", description = "用户rating积分")
public class UserRatingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户编号")
    private String userName;

    @ApiModelProperty(value = "比赛编号")
    private String contestTitle;

    @ApiModelProperty(value = "在本场比赛获得的积分")
    private Integer ratingChange;

}
