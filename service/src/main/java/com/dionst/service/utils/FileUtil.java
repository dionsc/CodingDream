package com.dionst.service.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtil {
    //在指定目录创建文件
    public static File createFile(String path) throws IOException {
        File file = new File(path);
        boolean newFile = file.createNewFile();
        if (!newFile) {
            throw new RuntimeException("Failed to create file " + file);
        }
        return file;
    }

    //将数据写入指定文件
    public static void writeToFile(String data, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        if (data != null)
            fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();
    }

    // 清空文件内容
    public static void clearFile(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
        fileOutputStream.close();
    }

    // 读取文件全部内容
    public static String readAllFile(File file) throws FileNotFoundException {
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
