package com.xinrui.component.strategy.core;

import com.alibaba.fastjson2.JSON;
import com.xinrui.component.strategy.IStrategyEnum;
import com.xinrui.component.strategy.config.StrategyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 策略查找器
 */
@Slf4j
public class StrategyBizExecutor implements ApplicationContextAware {


    private static ApplicationContext applicationContext;
    /**
     * 设置applicationContext
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StrategyBizExecutor.applicationContext=applicationContext;
    }

    /**
     * 优先配置的执行strategyBiz
     *
     * @param clazz
     * @return
     */
    public static <T> T execute(Class<T> clazz, IStrategyEnum strategyEnum) {

        log.info("strategy-查询执行策略入参：{}", JSON.toJSONString(strategyEnum));
        String beanName = StrategyConstants.parseBeanName(clazz.getName(), strategyEnum.strategyCode());
        if (applicationContext.containsBean(beanName)) {
            log.info("strategy 执行【{}】策略{}", clazz.getSimpleName(), strategyEnum.strategyCode());
            return applicationContext.getBean(beanName, clazz);
        }
        String errorMessage=String.format("strategy 未查询到执行【%s】策略 %s",clazz.getSimpleName(),strategyEnum.strategyCode());
        log.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }
}
