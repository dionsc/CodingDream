package com.dionst.service.utils;

import com.dionst.service.properties.JudgeProperties;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;

public class DockerUtil {
    public static String createContainer(DockerClient dockerClient, JudgeProperties judgeProperties, String workDir) {
        String image = judgeProperties.getSandboxImage();
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        //单位转换 MB -> B
        hostConfig.withMemory(judgeProperties.getContainerMemory() << 20L);
        hostConfig.withMemorySwap(0L);
        // 数据卷挂载
        hostConfig.setBinds(new Bind(workDir, new Volume("/app")));
        CreateContainerResponse exec = containerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withName(image)
                .withTty(true)
                .exec();
        //启动容器
        dockerClient.startContainerCmd(exec.getId()).exec();
        return exec.getId();
    }

    //与docker建立连接
    public static DockerClient dockerConnect() {
        // todo 测试代码
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return null;
        }
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        dockerClient.infoCmd().exec();
        return dockerClient;
    }

}
