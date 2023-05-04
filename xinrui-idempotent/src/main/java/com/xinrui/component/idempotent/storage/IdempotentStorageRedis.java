package com.xinrui.component.idempotent.storage;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author jerry
 */
@Slf4j
public class IdempotentStorageRedis implements IdempotentStorage {


    private RedissonClient redissonClient;

    public IdempotentStorageRedis(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public IdempotentStorageTypeEnum type() {
        return IdempotentStorageTypeEnum.REDIS;
    }

    @Override
    public void setValue(String key, String value, long expireTime, TimeUnit timeUnit) {
        log.debug("Redis Set key:{}, Value:{}, expireTime:{}, timeUnit:{}", key, value, expireTime, timeUnit);
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (bucket != null) {
            bucket.set(value, expireTime, timeUnit);
        }
    }

    @Override
    public String getValue(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        String value = bucket.get();
        log.debug("Redis Get key:{}, Value:{}", key, value);
        return value;
    }
}
