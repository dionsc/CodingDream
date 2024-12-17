package com.dionst.service.judge.compile;

import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.utils.FileUtil;

import java.io.File;
import java.io.IOException;

public class JavaCompiler implements CompilerStrategy {


    @Override
    public VerdictEnum compile(String code, String outputDir) throws IOException, InterruptedException {
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
