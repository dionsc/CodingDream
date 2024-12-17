package com.dionst.service.judge.run;

import com.dionst.service.model.enums.VerdictEnum;

import java.io.IOException;

public interface RunnerStrategy {

    VerdictEnum run(String code,
                    Long timeLimit,
                    Long memoryLimit,
                    String... otherParameter) throws IOException, InterruptedException;
}