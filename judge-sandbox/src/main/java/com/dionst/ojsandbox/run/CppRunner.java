package com.dionst.ojsandbox.run;

import com.dionst.ojsandbox.enums.VerdictEnum;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class CppRunner extends Runner {
    @Override
    public VerdictEnum run(Long timeLimit, Long memoryLimit, String... otherParameters) throws IOException, InterruptedException {

        String[] cmd = Stream.concat(
                        Arrays.stream(new String[]{dir + File.separator + "main"}),
                        Arrays.stream(otherParameters))
                .toArray(String[]::new);

        int exitCode = executeCMD(cmd, timeLimit, memoryLimit);

        //根据不同退出码判断运行结果
        if (exitCode == -5)
            return VerdictEnum.OutOfTimeError;
        if (exitCode == 0)
            return VerdictEnum.Accepted;
        if (exitCode == 137)
            return VerdictEnum.OutOfMemoryError;
        if (exitCode == 139)
            return VerdictEnum.SegmentError;
        if (exitCode == 20)//自定义退出码，用于判题使用
            return VerdictEnum.WrongAnswer;

        return VerdictEnum.RunError;
    }
}
