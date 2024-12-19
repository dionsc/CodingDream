package com.dionst.service.judge.run;

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


    public JavaRunner(String code) throws IOException, InterruptedException {
        super(code);
    }

    @Override
    public VerdictEnum compile() throws IOException, InterruptedException {
        String outputDir = dirFile.getAbsolutePath();
        //创建文件
        File file = new File(outputDir + File.separator + ".Main.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        String[] cmd = {"javac", file.getAbsolutePath()};
        FileUtil.writeToFile(code, file);
        // 执行命令
        Runtime runtime = Runtime.getRuntime();
        int w = runtime.exec(cmd).waitFor();
        if (w != 0) {
            return VerdictEnum.CompileError;
        }
        return VerdictEnum.Accepted;
    }

}
