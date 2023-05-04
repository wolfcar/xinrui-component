package com.xinrui.component.swak.core;


import com.alibaba.fastjson2.JSON;
import com.xinrui.component.swak.config.SwakConstants;
import com.xinrui.component.swak.context.SwakThreadContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

import static com.xinrui.component.swak.config.SwakConstants.SWAK_DEFAULT_BIZ;
import static com.xinrui.component.swak.config.SwakConstants.SWAK_DEFAULT_TAG;


/**
 * <b><code>LookupSwakBiz</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/25 14:45.
 *
 * @author jerry
 * @since guoke-component
 */
@Slf4j
public class LookupSwakBiz implements ApplicationContextAware {
    /**
     *
     */
    private static ApplicationContext applicationContext;

    /**
     * 优先配置的执行SwakBiz
     *
     * @param clazz
     * @return
     */
    public static <T> T lookupBean(Class<T> clazz, SwakThreadContext context) {
        log.info("swak-查询执行策略入参：{}", JSON.toJSONString(context));
        String beanName = SwakConstants.parseBeanName(clazz.getName(), context);
        if (applicationContext.containsBean(beanName)) {
            log.info("swak 执行【{}】策略,业务身份{}，场景:{}", clazz.getSimpleName(), context.getBizCode(), context.getTag());
            return applicationContext.getBean(beanName, clazz);
        } else if (!Objects.equals(context.getTag(), SWAK_DEFAULT_TAG)) {
            SwakThreadContext nextContext = new SwakThreadContext(context.getBizCode(), SWAK_DEFAULT_TAG);
            return lookupBean(clazz, nextContext);
        } else {
            SwakThreadContext nextContext = new SwakThreadContext(SWAK_DEFAULT_BIZ, context.getTag());
            return lookupBean(clazz, nextContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LookupSwakBiz.applicationContext = applicationContext;
    }
}
