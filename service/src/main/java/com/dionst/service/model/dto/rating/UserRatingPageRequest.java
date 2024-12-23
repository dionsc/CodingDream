package com.dionst.service.model.dto.rating;

import lombok.Data;

/**
 * 分页请求
 */
@Data
public class UserRatingPageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 用户编号
     */
    private Long userId;

}
