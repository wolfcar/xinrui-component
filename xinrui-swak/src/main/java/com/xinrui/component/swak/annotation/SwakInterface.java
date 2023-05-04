package com.xinrui.component.swak.annotation;


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
public @interface SwakInterface {


    /**
     * 接口功能描述
     *
     * @return
     */
    String desc() default "";

}
