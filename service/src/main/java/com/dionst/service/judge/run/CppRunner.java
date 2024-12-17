package com.dionst.service.judge.run;

import com.dionst.service.judge.compile.CppCompiler;
import com.dionst.service.model.enums.VerdictEnum;
import lombok.Getter;

import java.io.IOException;

@Getter
public class CppRunner extends Runner implements RunnerStrategy {

    private final CppCompiler compiler = new CppCompiler();

    public CppRunner() throws IOException {
        super();
    }

    @Override
    public VerdictEnum run(String code, Long timeLimit, Long memoryLimit, String... otherParameter) throws IOException, InterruptedException {
        return VerdictEnum.Accepted;
    }

}
