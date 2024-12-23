package com.dionst.service.model.dto.ranking;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 赛时提交
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Data
public class RankingPageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;


    /**
     * 比赛编号
     */
    private Long contestId;
}
