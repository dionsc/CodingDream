package com.dionst.service.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PageResult implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private List records;
}
