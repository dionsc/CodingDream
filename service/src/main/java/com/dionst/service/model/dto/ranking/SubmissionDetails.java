package com.dionst.service.model.dto.ranking;

import com.dionst.service.model.entity.Submission;
import lombok.Data;

import java.util.List;

@Data
public class SubmissionDetails {
    Long questionIndex;
    List<Submission> submissions;
    Long acceptedPenalty; //分钟
}