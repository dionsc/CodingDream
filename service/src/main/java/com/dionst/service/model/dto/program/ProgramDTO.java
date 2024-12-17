package com.dionst.service.model.dto.program;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 程序
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class ProgramDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "编程语言 枚举：1->java 2->cpp")
    private Integer language;

    @ApiModelProperty(value = "代码")
    private String code;

}
