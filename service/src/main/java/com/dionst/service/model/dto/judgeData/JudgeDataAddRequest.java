package com.dionst.service.model.dto.judgeData;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 判题数据
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class JudgeDataAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据名称")
    private String name;

    @ApiModelProperty(value = "数据对应题目")
    private Long questionId;

    @ApiModelProperty(value = "输入数据")
    private String input;

    @ApiModelProperty(value = "输出数据")
    private String output;

}
