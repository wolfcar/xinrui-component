package com.xinrui.component.swak.core;

import com.xinrui.component.swak.context.SwakThreadContext;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author 车德俊 chedejun@126.com
 * @module star-common
 * @date 2022/11/23 11:57
 * @slogan 生活不止眼前的苟且, 还有诗和远方的田野
 * @since
 */
@Component
public class SwakBizExecutor {


    /**
     * 获取具体策略
     *
     * @param interfaceClazz
     * @param bizCode
     * @param tag
     * @param <T>
     * @return
     */
    public static <T> T findSwakBiz(Class<T> interfaceClazz, String bizCode, String tag) {
        return LookupSwakBiz.lookupBean(interfaceClazz, new SwakThreadContext(bizCode, tag));
    }


    /**
     * 执行有返回值的扩展点
     *
     * @param interfaceClazz
     * @param bizCode
     * @param tag
     * @param function
     * @param <R>
     * @param <T>
     * @return
     */
    public <R, T> R execute(Class<T> interfaceClazz, String bizCode, String tag, Function<T, R> function) {
        T bean = LookupSwakBiz.lookupBean(interfaceClazz, new SwakThreadContext(bizCode, tag));
        return function.apply(bean);
    }


    /**
     * 执行无返回值的扩展点
     *
     * @param interfaceClazz
     * @param bizCode
     * @param tag
     * @param consumer
     * @param <T>
     */
    public <T> void executeVoid(Class<T> interfaceClazz, String bizCode, String tag, Consumer<T> consumer) {
        T bean = LookupSwakBiz.lookupBean(interfaceClazz, new SwakThreadContext(bizCode, tag));
        consumer.accept(bean);
    }




}
