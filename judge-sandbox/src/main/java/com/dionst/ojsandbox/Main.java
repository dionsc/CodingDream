package com.dionst.ojsandbox;

import com.dionst.ojsandbox.enums.VerdictEnum;
import com.dionst.ojsandbox.run.CppRunner;
import com.dionst.ojsandbox.run.JavaRunner;
import com.dionst.ojsandbox.run.Runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {


    private static final String dir = "./app";
    private static final File errorFile = new File(dir + File.separator + "error.txt");
    private static final File inputFile = new File(dir + File.separator + "input.txt");
    private static final File outputFile = new File(dir + File.separator + "output.txt");

    public static void main(String[] args) throws IOException, InterruptedException {

        Long timeLimit = Long.parseLong(args[0]);
        Long memoryLimit = Long.parseLong(args[1]);

        String[] strings = Arrays.copyOfRange(args, 2, args.length);

        String dir = "./app";
        Integer runCodeLanguage = Integer.parseInt(args[2]);

        String[] runJavaCmd = {"java", "-cp", dir, "com.dionst.ojsandbox.Main"};
        String[] runCppCmd = {dir + File.separator + "main"};

        VerdictEnum verdictEnum;

        Runner runner = null;

        switch (runCodeLanguage) {
            case 1:
                runner = new JavaRunner();
                break;
            case 2:
                runner = new CppRunner();
                break;
            default:

        }
        if (runner != null) {
            verdictEnum = runner.run(timeLimit, memoryLimit);
        } else {
            verdictEnum = VerdictEnum.SystemError;
        }
        writeResultToFile(verdictEnum);
    }


    private static void writeResultToFile(VerdictEnum verdictEnum) throws IOException {

        File file = new File(dir + File.separator + "system.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(String.valueOf(verdictEnum.getValue()).getBytes(StandardCharsets.UTF_8));
    }
}
