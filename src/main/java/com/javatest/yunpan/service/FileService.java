package com.javatest.yunpan.service;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    boolean checkFileExits(String md5);
    void saveFileByMD5(String md5, InputStream inputStream) throws IOException;
    void writeChunkToFileByMD5(String md5, InputStream inputStream, int chunk) throws IOException;
    boolean checkChunkExits(String md5, int id);
    void finishFileUpload(String md5);
}
