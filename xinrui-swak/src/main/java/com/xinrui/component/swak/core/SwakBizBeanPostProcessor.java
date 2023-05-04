package com.xinrui.component.swak.core;


import com.xinrui.component.swak.annotation.SwakBiz;
import com.xinrui.component.swak.config.SwakConstants;
import com.xinrui.component.swak.context.SwakThreadContext;
import com.xinrui.component.swak.exception.SwakInitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <b><code>SwakBizBeanPostProcessor</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/25 22:21.
 *
 * @author jerry
 * @since guoke-component
 */
@Slf4j
public class SwakBizBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware, SmartInitializingSingleton {

    private DefaultListableBeanFactory beanFactory;

    private final Set<Class> swakInterfaceClasses = new CopyOnWriteArraySet<>();

    /**
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory( BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();

        boolean isPresent = targetClass.isAnnotationPresent(SwakBiz.class);
        if (isPresent) {
            SwakBiz annotation = targetClass.getAnnotation(SwakBiz.class);
            String[] tags = annotation.tags();
            for (String tag : tags) {
                String newBeanName = SwakConstants.parseBeanName(targetClass.getInterfaces()[0].getName(), new SwakThreadContext(annotation.bizCode(), tag));
                beanFactory.registerSingleton(newBeanName, bean);
            }
            swakInterfaceClasses.add(targetClass.getInterfaces()[0]);
        }
        return bean;

    }

    @Override
    public void afterSingletonsInstantiated() {
        SwakThreadContext defaultContext=SwakThreadContext.defaultContext();
        List<String> errorMessage=new ArrayList<>();
        for (Class<?> clazz : swakInterfaceClasses) {
            String beanName = SwakConstants.parseBeanName(clazz.getName(), defaultContext);
            boolean containsBean = beanFactory.containsBean(beanName);
            if(!containsBean){
                errorMessage.add(clazz.getName());
            }
            if(!CollectionUtils.isEmpty(errorMessage)){
                String missDefaultSwakBizClassName = StringUtils.collectionToCommaDelimitedString(errorMessage);
                throw new SwakInitException("SWAK完整性检查失败，以下类缺失默认的SwakBiz实现"+missDefaultSwakBizClassName);
            }
        }
        log.info("SWAK完整性检查通过,扩展点：{} 个",swakInterfaceClasses.size());
    }
}
