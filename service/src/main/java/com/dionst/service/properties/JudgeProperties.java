package com.dionst.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "judge")
public class JudgeProperties {

    //沙箱镜像
    private String sandboxImage;

    //容器内存大小(单位：MB)
    private Long containerMemory;
}