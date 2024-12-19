package com.dionst.ojsandbox.run;

import com.dionst.ojsandbox.enums.VerdictEnum;

import java.io.IOException;

public interface RunnerStrategy {

    VerdictEnum run(Long timeLimit, Long memoryLimit, String... otherParameters) throws IOException, InterruptedException;


}