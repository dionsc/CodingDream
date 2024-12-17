package com.dionst.service.judge.run;

import com.dionst.service.judge.compile.JavaCompiler;
import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.utils.FileUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class JavaRunner extends Runner implements RunnerStrategy {

    private final JavaCompiler compiler = new JavaCompiler();

    public JavaRunner() throws IOException {
        super();
    }

    @Override
    public VerdictEnum run(String code, Long timeLimit, Long memoryLimit, String... otherParameter) throws IOException, InterruptedException {
        JavaCompiler compiler = this.getCompiler();
        File systemFile = this.getSystemFile();
        File dirFile = this.getDirFile();
        DockerClient dockerClient = this.getDockerClient();
        String containerId = this.getContainerId();

        compiler.compile(code, dirFile.getAbsolutePath());
        //创建沙箱中的执行命令
        String[] cmdArray = {"java", "-cp", "/app/", "Judge",
                String.valueOf(timeLimit),
                String.valueOf(memoryLimit)};

        String[] cmd = Stream.concat(Arrays.stream(cmdArray), Arrays.stream(otherParameter)).toArray(String[]::new);

        String execId = dockerClient.execCreateCmd(containerId)
                .withCmd(cmd)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .exec().getId();
        //执行
        ExecStartCmd execStartCmd = dockerClient.execStartCmd(execId);
        execStartCmd.exec(new ExecStartResultCallback()).awaitCompletion();

        //获取执行结果
        int result = Integer.parseInt(Objects.requireNonNull(FileUtil.readAllFile(systemFile)));

        return VerdictEnum.getEnumByValue(result);
    }

}
