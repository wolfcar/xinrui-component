package com.xinrui.component.strategy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略业务实现
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyBiz {

    /**
     * 策略编码
     * @return
     */
    String code();

    /**
     * 策略名称
     * @return
     */
    String name() default "";
}
