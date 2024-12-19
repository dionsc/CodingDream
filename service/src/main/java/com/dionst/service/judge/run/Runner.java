package com.dionst.service.judge.run;

import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.utils.DockerUtil;
import com.dionst.service.utils.FileUtil;
import com.dionst.service.properties.JudgeProperties;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;


@Data
public abstract class Runner implements RunnerStrategy {

    @Autowired
    private JudgeProperties judgeProperties;

    public String uniqueMark;
    private File errorFile;
    private File outputFile;
    private File userOutputFile;
    private File inputFile;
    private File systemFile;
    protected File dirFile;
    private DockerClient dockerClient;
    private String containerId;
    private String workDir;
    public String code;

    public Runner(String code) throws IOException, InterruptedException {
        this.code = code;
        uniqueMark = UUID.randomUUID().toString();
        //工作目录
        workDir = "." + File.separator + uniqueMark;
        //创建目录
        dirFile = new File(workDir);
        if (!dirFile.exists()) {
            boolean mkdirs = dirFile.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("Failed to create directory " + dirFile);
            }
        }
        workDir = dirFile.getAbsolutePath();
        //创建输入文件
        inputFile = FileUtil.createFile(workDir + File.separator + "input.txt");
        //创建输出文件
        outputFile = FileUtil.createFile(workDir + File.separator + "output.txt");
        //创建异常处理文件
        errorFile = FileUtil.createFile(workDir + File.separator + "error.txt");
        //创建判题信息暂存1文件
        systemFile = FileUtil.createFile(workDir + File.separator + "system.txt");
        //创建判题时存放用户输出数据的文件
        userOutputFile = FileUtil.createFile(workDir + File.separator + "userOutput.txt");
        //与docker建立连接
        dockerClient = DockerUtil.dockerConnect();
        //创建容器
        containerId = DockerUtil.createContainer(dockerClient, judgeProperties, workDir);

        /**
         * 编译代码
         */
        VerdictEnum compile = compile();
        if (!VerdictEnum.Accepted.equals(compile)) {
            throw new RuntimeException(VerdictEnum.CompileError.getText());
        }

    }

    public VerdictEnum run(Long timeLimit, Long memoryLimit, String... otherParameter) throws IOException, InterruptedException {


        File systemFile = this.getSystemFile();
        File dirFile = this.getDirFile();


        DockerClient dockerClient = this.getDockerClient();
        String containerId = this.getContainerId();

        //创建沙箱中的执行命令
        String[] cmdArray = {"java", "-jar", "/app/judge-sandbox-1.0-SNAPSHOT.jar",
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
