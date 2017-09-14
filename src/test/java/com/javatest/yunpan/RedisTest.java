package com.javatest.yunpan;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void connectTest() {
        stringRedisTemplate.opsForValue().set("test", "success");
        Assert.assertEquals("success", stringRedisTemplate.opsForValue().get("test"));
    }

    @Test
    public void ObjectCRUDTest() {
        String key = "md5";
        SetOperations<String, Integer> setOperations = redisTemplate.opsForSet();

        setOperations.add(key, 1);
        setOperations.add(key, 2);
        Assert.assertEquals(new Long(2), setOperations.size(key));
        Assert.assertEquals(true, setOperations.isMember(key, 1));
        Assert.assertEquals(true, setOperations.isMember(key, 2));
        Assert.assertEquals(false, setOperations.isMember(key, 3));
    }
}
