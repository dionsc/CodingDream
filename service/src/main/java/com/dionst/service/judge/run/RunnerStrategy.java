package com.dionst.service.judge.run;

import com.dionst.service.model.enums.VerdictEnum;

import java.io.IOException;

public interface RunnerStrategy {

    VerdictEnum compile() throws IOException, InterruptedException;
}