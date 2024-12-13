package com.dionst.service.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("验证码")
    private String code;
}
