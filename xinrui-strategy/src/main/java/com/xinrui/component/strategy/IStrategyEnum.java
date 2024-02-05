package com.xinrui.component.strategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略枚举超类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IStrategyEnum {


    /**
     * 获取策略编码
     * @return
     */
    String strategyCode();
}
