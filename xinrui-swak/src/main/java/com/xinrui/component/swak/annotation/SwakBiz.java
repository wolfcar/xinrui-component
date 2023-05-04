package com.xinrui.component.swak.annotation;

import com.xinrui.component.swak.config.SwakConstants;
import org.springframework.stereotype.Component;

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
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface SwakBiz {

    /**
     * 业务线
     *
     * @return
     */
    String bizCode() default SwakConstants.SWAK_DEFAULT_BIZ;


    /**
     * 要实现的 tag
     *
     * @return
     */
    String[] tags() default {SwakConstants.SWAK_DEFAULT_TAG};


}
