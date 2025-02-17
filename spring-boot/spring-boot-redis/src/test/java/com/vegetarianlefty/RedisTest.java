package com.vegetarianlefty;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * description
 *
 * @date 2025/2/10 10:16
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest()
public class RedisTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testRedis()
    {
        stringRedisTemplate.opsForValue().set("k0","v0");
        String v = stringRedisTemplate.opsForValue().get("k0");
        log.info(v);
    }
}
