package com.javatest.yunpan.util;

import java.io.*;

public class FileReceiver {

    private RandomAccessFile randomAccessFile;
    private String md5;
    private InputStream inputStream;
    private int chunk;


    public FileReceiver(String md5, InputStream inputStream, int chunk) {
        this.md5 = md5;
        this.inputStream = inputStream;
        this.chunk = chunk;
    }

    public FileReceiver(String md5, InputStream inputStream) {
        this(md5, inputStream, 0);
    }

    public void run() throws IOException {
        File file = new File("D:\\JetBrains\\IdeaProjects\\yunpan\\src\\main\\resources\\file_temp" + File.separator + md5);
        if (!file.exists()) {
            file.createNewFile();
        }
        randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(5242880 * chunk);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            randomAccessFile.write(buffer, 0, len);
        }
    }
}
