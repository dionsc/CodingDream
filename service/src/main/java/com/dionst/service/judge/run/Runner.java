package com.dionst.service.judge.run;

import com.dionst.service.utils.DockerUtil;
import com.dionst.service.utils.FileUtil;
import com.dionst.service.properties.JudgeProperties;
import com.github.dockerjava.api.DockerClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.*;


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
    private File dirFile;
    private DockerClient dockerClient;
    private String containerId;
    private String workDir;

    public Runner() throws IOException {
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
    }
}
