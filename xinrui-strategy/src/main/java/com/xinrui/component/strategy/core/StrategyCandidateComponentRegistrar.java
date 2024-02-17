package com.xinrui.component.strategy.core;


import com.xinrui.component.common.spring.AbstractCandidateComponentRegistrar;
import com.xinrui.component.strategy.annotation.StrategyInterface;
import com.xinrui.component.strategy.annotation.StrategyScan;
import com.xinrui.component.strategy.config.StrategyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 策略类注册
 */
@Slf4j
public class StrategyCandidateComponentRegistrar extends AbstractCandidateComponentRegistrar {

    /**
     * 注册策略类
     *
     * @param registry
     * @param importBeanNameGenerator
     * @param candidate
     */
    @Override
    protected void registerBeanDefinition(BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator, BeanDefinition candidate) {
        AnnotationMetadata strategyInterfaceAnno = null;
        if (candidate instanceof AnnotatedBeanDefinition) {
            strategyInterfaceAnno = ((AnnotatedBeanDefinition) candidate).getMetadata();
        }
        assert strategyInterfaceAnno != null;
        registerStrategyInterface(registry, candidate, strategyInterfaceAnno);

    }

    /**
     * 注册策略接口
     *
     * @param registry
     * @param candidate
     * @param strategyInterfaceAnno
     */
    private void registerStrategyInterface(BeanDefinitionRegistry registry, BeanDefinition candidate, AnnotationMetadata strategyInterfaceAnno) {
        boolean annotated = strategyInterfaceAnno.isAnnotated(StrategyInterface.class.getName());
        if (annotated) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(StrategyProxyFactoryBean.class);
            beanDefinition.getPropertyValues().addPropertyValue("strategyInterfaceClass", candidate.getBeanClassName());
            beanDefinition.setPrimary(true);

            String beanName = StrategyConstants.parseProxyBeanName(candidate.getBeanClassName());
            registry.registerBeanDefinition(beanName, beanDefinition);

        }
    }

    /**
     * 生成过滤器
     *
     * @param annotationTypeFilters
     */
    @Override
    protected void generateFilters(List<AnnotationTypeFilter> annotationTypeFilters) {
        annotationTypeFilters.add(new AnnotationTypeFilter(StrategyInterface.class));

    }

    /**
     * 解析 AnnotationMetadata 获取需要扫描的类，返回包路径，
     *
     * @param importingClassMetadata
     * @return
     */
    @Override
    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(StrategyScan.class.getCanonicalName());

        assert attributes != null;

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get(StrategyConstants.BASE_PACKAGES)) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            String packageName = ClassUtils.getPackageName(importingClassMetadata.getClassName());
            log.info("strategy未指定baseScanPackages，使用默认包路径,默认加载：{}", packageName);
            basePackages.add(packageName);
        }
        log.info("strategy扫描包路径：{}", basePackages);
        return basePackages;
    }
}
