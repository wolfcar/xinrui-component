package com.xinrui.component.strategy.config;


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
public class StrategyConstants {



    public static final String BASE_PACKAGES = "basePackages";


    public static String parseProxyBeanName(String className) {
        return String.format("%s#strategy#proxy", className);
    }

    /**
     *
     * @param name
     * @param strategyEnum
     * @return
     */
    public static String parseBeanName(String name, String strategyCode) {
        return String.format("%s#%s", name, strategyCode);
    }
}
