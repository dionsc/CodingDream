package com.dionst.service.judge.compile;

import com.dionst.service.model.enums.VerdictEnum;

import java.io.IOException;

public interface CompilerStrategy {
    VerdictEnum compile(String code, String outputDir) throws IOException, InterruptedException;
} 