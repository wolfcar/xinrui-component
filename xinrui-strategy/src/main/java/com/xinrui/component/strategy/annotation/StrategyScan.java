package com.xinrui.component.strategy.annotation;


import com.xinrui.component.strategy.config.StrategyConfiguration;
import com.xinrui.component.strategy.core.StrategyBizBeanPostProcessor;
import com.xinrui.component.strategy.core.StrategyCandidateComponentRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:扫描策略接口
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({StrategyCandidateComponentRegistrar.class, StrategyBizBeanPostProcessor.class, StrategyConfiguration.class})
public @interface StrategyScan {
}
