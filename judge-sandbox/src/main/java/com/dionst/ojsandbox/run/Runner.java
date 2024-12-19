package com.dionst.ojsandbox.run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public abstract class Runner implements RunnerStrategy {


    public static final String dir = "./app";
    public static final File errorFile = new File(dir + File.separator + "error.txt");
    public static final File inputFile = new File(dir + File.separator + "input.txt");
    public static final File outputFile = new File(dir + File.separator + "output.txt");

    /*
    超时返回 -5
    其他情况返回退出码
     */
    public static int executeCMD(String[] cmd, Long timeLimit, Long memoryLimit) throws IOException, InterruptedException {

        //创建设置cgroup,限制程序占用内存
        String[] cgroup1 = {"sudo", "cgcreate", "-g", "memory:/mygroup"};
        String[] cgroup2 = {"sudo", "cgset", "-r", "memory.limit_in_bytes=" + memoryLimit + "M", "mygroup"};
        int e1 = new ProcessBuilder(cgroup1).start().waitFor();
        int e2 = new ProcessBuilder(cgroup2).start().waitFor();


        ProcessBuilder processBuilder = new ProcessBuilder(
                Stream.concat(Arrays.stream(new String[]{"sudo", "cgexec", "-g", "memory:mygroup"}), Arrays.stream(cmd))
                        .toArray(String[]::new));
        // 设置重定向输入输出
        processBuilder.redirectInput(inputFile); // 重定向输入
        processBuilder.redirectOutput(outputFile); // 重定向输出
        processBuilder.redirectError(errorFile); // 重定向错误输出

        Process runProcess = processBuilder.start();


        // 超时控制
        final boolean[] isTimedOut = {false};
        new Thread(() -> {
            try {
                Thread.sleep(timeLimit);
                runProcess.destroyForcibly();
                isTimedOut[0] = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        int exitCode = runProcess.waitFor();
        //判断是否超时
        if (isTimedOut[0]) {
            return -5;
        }
        return exitCode;
    }


    public String readAllFile(File file) throws FileNotFoundException {
        if (!file.exists() || !file.isFile()) return null;
        Scanner scanner = new Scanner(file);
        List<String> output = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            output.add(data);
        }
        return String.join("\n", output);
    }
}
