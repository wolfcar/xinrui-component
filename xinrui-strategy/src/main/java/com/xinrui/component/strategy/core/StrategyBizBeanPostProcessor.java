package com.xinrui.component.strategy.core;

import com.xinrui.component.strategy.annotation.StrategyBiz;
import com.xinrui.component.strategy.config.StrategyConstants;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 策略bean注册到spring容器
 */
public class StrategyBizBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;


    /**
     * 设置beanFactory
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    /**
     * bean初始化之前
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * bean初始化之后
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();

        boolean isPresent = targetClass.isAnnotationPresent(StrategyBiz.class);
        if (isPresent) {
            StrategyBiz annotation = targetClass.getAnnotation(StrategyBiz.class);
            String code = annotation.code();

            String newBeanName = StrategyConstants.parseBeanName(targetClass.getInterfaces()[0].getName(), code);
            beanFactory.registerSingleton(newBeanName, bean);

        }
        return bean;

    }
}
