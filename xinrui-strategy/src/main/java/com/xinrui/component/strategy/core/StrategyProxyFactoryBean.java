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

@Slf4j
public class StrategyProxyFactoryBean implements FactoryBean, InvocationHandler, BeanClassLoaderAware {

    private ClassLoader classLoader;
    @Setter
    private Class<?> strategyInterfaceClass;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("strategy proxy invoke:{}#{}", strategyInterfaceClass.getSimpleName(),method.getName());
        }

        Class<?> declaringClass = method.getDeclaringClass();

        Object arg = args[0];
        if(!(arg instanceof IStrategyEnum)){
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

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader=classLoader;

    }

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(this.classLoader, new Class[]{strategyInterfaceClass}, this);
    }

    @Override
    public Class<?> getObjectType() {
        return this.strategyInterfaceClass;
    }


}
