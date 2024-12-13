package com.dionst.service.model.dto.contest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.aspectj.bridge.IMessage;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 比赛
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
@NoArgsConstructor
public class ContestAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "比赛描述")
    private String description;

    @NotNull
    @ApiModelProperty(value = "比赛标题")
    private String title;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")//"2025-01-01 00:00"
    @ApiModelProperty(value = "比赛开始时间")
    private LocalDateTime startTime;

    @NotNull
    @ApiModelProperty(value = "比赛时长(单位:分钟)")
    private Long duration;

}
