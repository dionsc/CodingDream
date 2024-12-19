package com.dionst.ojsandbox.run;

import com.dionst.ojsandbox.enums.VerdictEnum;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class JavaRunner extends Runner {
    @Override
    public VerdictEnum run(Long timeLimit, Long memoryLimit, String... otherParameters) throws IOException, InterruptedException {

        String[] cmd = Stream.concat(
                        Arrays.stream(new String[]{"java", "-cp", Runner.dir, "Main"}),
                        Arrays.stream(otherParameters))
                .toArray(String[]::new);

        int exitCode = Runner.executeCMD(cmd, timeLimit, memoryLimit);
        if (exitCode == -5) return VerdictEnum.OutOfTimeError;

        //查看是否存在异常
        String error = readAllFile(Runner.errorFile);
        if (error != null && !error.isEmpty()) {
            if (error.contains("OutOfMemoryError")) {
                return VerdictEnum.OutOfTimeError;
            } else if (error.contains("StackOverflowError")) {
                return VerdictEnum.StackOverflowError;
            } else if (error.contains("WrongAnswer")) {
                return VerdictEnum.WrongAnswer;
            } else {
                return VerdictEnum.RunError;
            }
        }
        return VerdictEnum.Accepted;
    }
}
