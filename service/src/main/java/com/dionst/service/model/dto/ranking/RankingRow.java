package com.dionst.service.model.dto.ranking;

import lombok.Data;

import java.util.List;


@Data
public class RankingRow {
    long rank;
    long penalty;
    long accepted;
    String userName;
    List<SubmissionDetails> submissionDetails;
}
