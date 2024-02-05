package com.xinrui.component.strategy.core;


import com.xinrui.component.common.spring.AbstractCandidateComponentRegistrar;
import com.xinrui.component.strategy.annotation.StrategyInterface;
import com.xinrui.component.strategy.annotation.StrategyScan;
import com.xinrui.component.strategy.config.StrategyConstants;
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


public class StrategyCandidateComponentRegistrar extends AbstractCandidateComponentRegistrar {
    @Override
    protected void registerBeanDefinition(BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator, BeanDefinition candidate) {
        AnnotationMetadata strategyInterfaceAnno = null;
        if (candidate instanceof AnnotatedBeanDefinition) {
            strategyInterfaceAnno = ((AnnotatedBeanDefinition) candidate).getMetadata();
        }
        assert strategyInterfaceAnno != null;
        registerStrategyInterface(registry, candidate, strategyInterfaceAnno);

    }

    private void registerStrategyInterface(BeanDefinitionRegistry registry, BeanDefinition candidate, AnnotationMetadata strategyInterfaceAnno) {
        boolean annotated = strategyInterfaceAnno.isAnnotated(StrategyInterface.class.getName());
        if (annotated) {
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(StrategyInterface.class);
            beanDefinition.getPropertyValues().addPropertyValue("strategyInterfaceClass", candidate.getBeanClassName());
            beanDefinition.setPrimary(true);

            String beanName = StrategyConstants.parseProxyBeanName(candidate.getBeanClassName());
            registry.registerBeanDefinition(beanName, beanDefinition);

        }
    }

    @Override
    protected void generateFilters(List<AnnotationTypeFilter> annotationTypeFilters) {
        annotationTypeFilters.add(new AnnotationTypeFilter(StrategyInterface.class));

    }

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
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }
}
