package com.dionst.service.model.dto.program;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class ProgramAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "编程语言 枚举：1->java 2->cpp")
    private String language;

    @ApiModelProperty(value = "代码")
    private String code;

}
