
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
 * 用户
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "关注数")
    @TableField("follow_number")
    private Long followNumber;

    @ApiModelProperty(value = "粉丝数")
    @TableField("fans_number")
    private Long fansNumber;

    @ApiModelProperty(value = "用户角色 枚举：0->普通 1->管理员 2->封号")
    private String userRole;

    @ApiModelProperty(value = "用户昵称")
    @TableField("nickname")
    private String nickname;

}
