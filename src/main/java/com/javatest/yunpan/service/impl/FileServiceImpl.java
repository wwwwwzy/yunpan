package com.javatest.yunpan.service.impl;

import com.javatest.yunpan.service.FileService;
import com.javatest.yunpan.util.FileReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public boolean checkFileExits(String md5) {
        return false;
    }

    @Override
    public void saveFileByMD5(String md5, InputStream inputStream) throws IOException {
        new FileReceiver(md5,inputStream).run();
    }

    @Override
    public void finishFileUpload(String md5) {
        SetOperations setOperations = redisTemplate.opsForSet();
        Iterator iterator = setOperations.members(md5).iterator();
        while (iterator.hasNext()) {
            setOperations.remove(md5, iterator.next());
        }
    }

    @Override
    public void writeChunkToFileByMD5(String md5, InputStream inputStream, int chunk) throws IOException {
        new FileReceiver(md5, inputStream, chunk).run();
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(md5, chunk);
    }

    @Override
    public boolean checkChunkExits(String md5, int id) {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(md5, id);
    }
}
