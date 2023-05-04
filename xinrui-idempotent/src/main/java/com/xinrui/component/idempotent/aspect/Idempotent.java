package com.xinrui.component.idempotent.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b><code>Idempotent</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * 注解类
 * <p/>
 * <b>Creation Time:</b> 2023/4/27 00:21
 *
 * <b>@author</b> jerry chedejun@126.com
 * <b>@since</b> 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 幂等主键,SPEL表达式，获取幂等Key
     * 默认规则为 ${applicationName}:key
     *
     * @return
     */
    String key();

    /**
     * 尝试加锁等待时间,默认为0，加锁失败立刻返回
     */
     int lockWaitTime() default 0;

    /**
     * 是否重复请求返回相同结果
     * @return
     */
    boolean repeat() default false;
    /**
     * 锁的过期时间，默认为0，开启看门狗，非0则看门狗失效
     *
     * @return
     */
    int lockExpireTime() default 0;

    /**
     * 一级存储过期时间 60秒
     *
     * @return
     */
    int firstLevelExpireTime() default 60;

    /**
     * 二级存储过期时间，默认一年
     *
     * @return
     */
    int secondLevelExpireTime() default 60 * 60 * 24 * 365;

    /**
     * 触发幂等限制时调用同类中的方法进行后续处理
     *
     * @return
     */
    String idempotentHandler() default "";


    /**
     * 触发幂等限制时调用其他类中的方法进行后续处理
     *
     * @return
     */
    Class<?>[] idempotentHandlerClass() default {};
}
