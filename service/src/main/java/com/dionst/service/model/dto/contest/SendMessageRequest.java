package com.dionst.service.model.dto.contest;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendMessageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long contestId;
    private String message;
}
