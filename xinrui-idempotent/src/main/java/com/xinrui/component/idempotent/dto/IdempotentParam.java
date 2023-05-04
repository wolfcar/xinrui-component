package com.xinrui.component.idempotent.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <b><code>IdempotentRequest</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/4/27 01:20
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class IdempotentParam<T> {

    /**
     * 系统
     */
    private String applicationName;
    /**
     * 幂等Key
     */
    private String idempotentKey;

    /**
     * 幂等Key
     */
    private String lockKey;

    /**
     * 尝试加锁等待时间
     */
    private int lockWaitTime;
    /**
     * 一级存储过期时间
     */
    private int firstLevelExpireTime;

    /**
     * 二级存储过期时间
     */
    private int secondLevelExpireTime;

    /**
     * 锁的过期时间
     */
    private int lockExpireTime;

    private Class<T> resultType;

    /**
     * 触发幂等限制时调用同类中的方法进行后续处理
     *
     * @return
     */
   private String idempotentHandler;


    /**
     * 触发幂等限制时调用其他类中的方法进行后续处理
     *
     * @return
     */
   private Class<?>[] idempotentHandlerClass;

   private boolean repeat;
}
