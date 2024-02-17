package com.xinrui.component.strategy.core;

import com.xinrui.component.strategy.IStrategyEnum;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 策略代理工厂
 */
@Slf4j
public class StrategyProxyFactoryBean implements FactoryBean, InvocationHandler, BeanClassLoaderAware {

    private ClassLoader classLoader;
    @Setter
    private Class<?> strategyInterfaceClass;

    /**
     * 代理方法
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("strategy proxy invoke:{}#{}", strategyInterfaceClass.getSimpleName(), method.getName());
        }

        Class<?> declaringClass = method.getDeclaringClass();

        Object arg = args[0];
        if (!(arg instanceof IStrategyEnum)) {
            throw new RuntimeException("获取策略失败，方法定义有误，方法需以实现IStrategyEnum接品的枚举做第一个参数");
        }


        Object bean = StrategyBizExecutor.execute(declaringClass, (IStrategyEnum) arg);
        // 获取方法调用返回
        Method targetMethod = bean.getClass().getMethod(method.getName(),
                method.getParameterTypes());
        try {
            return targetMethod.invoke(bean, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

    }

    /**
     * 设置classLoader
     *
     * @param classLoader
     */
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;

    }

    /**
     * 获取代理对象
     *
     * @return
     */
    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(this.classLoader, new Class[]{strategyInterfaceClass}, this);
    }

    /**
     * 获取代理对象类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return this.strategyInterfaceClass;
    }


}
