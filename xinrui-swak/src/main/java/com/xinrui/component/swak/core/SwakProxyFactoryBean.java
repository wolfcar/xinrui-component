package com.xinrui.component.swak.core;


import com.xinrui.component.swak.context.SwakContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <b><code>SwakProxyFactoryBean</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/24 17:24.
 *
 * @author jerry
 * @since guoke-component
 */
@Slf4j
public class SwakProxyFactoryBean implements FactoryBean, InvocationHandler, BeanClassLoaderAware {

    private ClassLoader classLoader;
    @Setter
    private Class<?> swakInterfaceClass;




    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (log.isDebugEnabled()) {
            log.debug("SwakBiz proxy invoke:{}", method.getName());
        }
        Class<?> declaringClass = method.getDeclaringClass();
        Object bean = LookupSwakBiz.lookupBean(declaringClass, SwakContext.get());
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
    public Object getObject() {
        return Proxy.newProxyInstance(classLoader, new Class[]{swakInterfaceClass}, this);
    }

    @Override
    public Class<?> getObjectType() {
        return swakInterfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }



}
