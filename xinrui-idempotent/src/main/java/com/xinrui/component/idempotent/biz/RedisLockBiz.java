package com.xinrui.component.idempotent.biz;

import com.xinrui.component.idempotent.dto.IdempotentParam;
import com.xinrui.component.idempotent.exception.DistributeLockFailException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <b><code>RedisLockBiz</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/4/27 01:19
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */
@Slf4j
public class RedisLockBiz {

    private final RedissonClient redissonClient;

    private final IdempotentBiz idempotentBiz;


    public RedisLockBiz(RedissonClient redissonClient, IdempotentBiz idempotentBiz) {
        this.redissonClient = redissonClient;
        this.idempotentBiz = idempotentBiz;
    }

    /**
     * 加锁后幂等操作
     *
     * @param param
     * @param lockSuccess
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T> T lockAfterIdempotent(IdempotentParam<T> param, Supplier<T> lockSuccess) {
        boolean locked;

        RLock rLock = redissonClient.getLock(param.getLockKey());

        if (param.getLockExpireTime() > 0) {
            locked = rLock.tryLock(param.getLockWaitTime(), param.getLockExpireTime(), TimeUnit.SECONDS);
        } else {
            locked = rLock.tryLock(param.getLockWaitTime(), TimeUnit.SECONDS);
        }
        if (locked) {
            try {
                idempotentBiz.idemponent(param, lockSuccess);
            } catch (Exception ex) {
                log.error("幂等业务逻辑异常:", ex);
                throw ex;
            } finally {
                if (rLock.getHoldCount() != 0) {
                    rLock.unlock();
                }
            }
        }
        String message = param.getLockKey() + "正在执行处理中";
        throw new DistributeLockFailException(message);


    }

}
