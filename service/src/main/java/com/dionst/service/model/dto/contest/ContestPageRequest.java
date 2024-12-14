package com.dionst.service.model.dto.contest;

import lombok.Data;

/**
 * 分页请求
 *
 */
@Data
public class ContestPageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 比赛标题
     */
    private String title = "";

    /**
     * 是否升序
     */
    private boolean asc = true;

}
