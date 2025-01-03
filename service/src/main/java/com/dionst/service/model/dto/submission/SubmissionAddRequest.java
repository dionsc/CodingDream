package com.dionst.service.model.dto.submission;

import com.baomidou.mybatisplus.annotation.*;
import com.dionst.service.model.dto.program.ProgramAddRequest;
import com.dionst.service.model.entity.Program;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 赛时提交
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class SubmissionAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "题目编号")
    private Long questionId;

    @ApiModelProperty(value = "提交的代码")
    private ProgramAddRequest code;

}
