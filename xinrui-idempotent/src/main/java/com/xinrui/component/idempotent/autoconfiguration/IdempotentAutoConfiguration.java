package com.xinrui.component.idempotent.autoconfiguration;

import com.xinrui.component.idempotent.aspect.DistributedIdempotentAspect;
import com.xinrui.component.idempotent.biz.IdempotentBiz;
import com.xinrui.component.idempotent.biz.RedisLockBiz;
import com.xinrui.component.idempotent.storage.IdempotentStorageFactory;
import com.xinrui.component.idempotent.storage.IdempotentStorageMysql;
import com.xinrui.component.idempotent.storage.IdempotentStorageRedis;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <b><code>IdempotentAutoConfiguration</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/5/4 13:57
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */
@Configuration
@ImportAutoConfiguration(IdempotentProperties.class)
public class IdempotentAutoConfiguration {

    @ConditionalOnBean(JdbcTemplate.class)
    @Bean
    public IdempotentStorageMysql idempotentStorageMysql(JdbcTemplate jdbcTemplate) {
        return new IdempotentStorageMysql(jdbcTemplate);
    }

    @Bean
    public IdempotentStorageRedis idempotentStorageRedis(RedissonClient redissonClient) {
        return new IdempotentStorageRedis(redissonClient);
    }

    @Bean
    public IdempotentStorageFactory idempotentStorageFactory() {

        return new IdempotentStorageFactory();
    }

    @Bean
    public IdempotentBiz idempotentBiz(IdempotentProperties idempotentProperties, IdempotentStorageFactory idempotentStorageFactory) {
        return new IdempotentBiz(idempotentStorageFactory, idempotentProperties);
    }

    @Bean
    public RedisLockBiz redisLockBiz(RedissonClient redissonClient, IdempotentBiz idempotentBiz) {
        return new RedisLockBiz(redissonClient, idempotentBiz);
    }

    @Bean
    public DistributedIdempotentAspect distributedIdempotentAspect(RedisLockBiz redisLockBiz) {
        return new DistributedIdempotentAspect(redisLockBiz);
    }


}
