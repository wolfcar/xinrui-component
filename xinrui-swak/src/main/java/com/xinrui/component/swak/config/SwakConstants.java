package com.xinrui.component.swak.config;


import com.xinrui.component.swak.context.SwakThreadContext;

/**
 * <b><code>SwakConstants</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/24 16:16.
 *
 * @author jerry
 * @since guoke-component
 */
public class SwakConstants {

    /**
     *
     */
   public static final String SWAK_DEFAULT_BIZ = "all";
    /**
     *
     */
   public static final  String SWAK_DEFAULT_TAG = "all";

   public static final String BASE_PACKAGES ="basePackages";

   public  static String parseBeanName(String className, SwakThreadContext context){
       return String.format("%s#swak-%s-%s",className,context.getBizCode(),context.getTag());
   }

   public  static String parseProxyBeanName(String className){
        return String.format("%s#swak#proxy",className);
   }
}
