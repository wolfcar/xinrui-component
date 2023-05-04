package com.xinrui.component.swak.annotation;

import com.xinrui.component.swak.config.SwakConfiguration;
import com.xinrui.component.swak.core.SwakBizBeanPostProcessor;
import com.xinrui.component.swak.core.SwakCandidateComponentRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b><code>SwakScan</code></b>
 * <p/>
 * Description
 * 扩展点接口注解
 * <p/>
 * <b>Creation Time:</b> 2022/10/24 16:13.
 *
 * @author jerry
 * @since guoke-component
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ SwakConfiguration.class, SwakCandidateComponentRegistrar.class, SwakBizBeanPostProcessor.class})
public @interface SwakScan {

    String[] basePackages() default {};


}
