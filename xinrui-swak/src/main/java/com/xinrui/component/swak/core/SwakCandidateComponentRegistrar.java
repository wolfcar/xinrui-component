package com.xinrui.component.swak.core;


import com.xinrui.component.common.spring.AbstractCandidateComponentRegistrar;
import com.xinrui.component.swak.annotation.SwakInterface;
import com.xinrui.component.swak.annotation.SwakScan;
import com.xinrui.component.swak.config.SwakConstants;
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
 * <b><code>SwakDefinitionRegistryPostProcessor</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/24 22:04.
 *
 * @author jerry
 * @since  1.0.0
 */
public class SwakCandidateComponentRegistrar extends AbstractCandidateComponentRegistrar {

    @Override
    protected void registerBeanDefinition(BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator, BeanDefinition candidate) {
        AnnotationMetadata swakAnno = null;
        if (candidate instanceof AnnotatedBeanDefinition) {
            swakAnno = ((AnnotatedBeanDefinition) candidate).getMetadata();
        }
        assert swakAnno != null;
        registerSwakInterface(registry, candidate, swakAnno);
    }

    @Override
    protected void generateFilters(List<AnnotationTypeFilter> annotationTypeFilters) {
        annotationTypeFilters.add(new AnnotationTypeFilter(SwakInterface.class));
    }

    @Override
    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(SwakScan.class.getCanonicalName());

        assert attributes != null;
        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get(SwakConstants.BASE_PACKAGES)) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


    private static void registerSwakInterface(BeanDefinitionRegistry registry, BeanDefinition candidate, AnnotationMetadata spiAnno) {
        boolean annotation = spiAnno.hasAnnotation(SwakInterface.class.getName());
        if (annotation) {

            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(SwakProxyFactoryBean.class);
            beanDefinition.getPropertyValues().addPropertyValue("swakInterfaceClass", candidate.getBeanClassName());
            beanDefinition.setPrimary(true);

            String beanName = SwakConstants.parseProxyBeanName(candidate.getBeanClassName());
            registry.registerBeanDefinition(beanName, beanDefinition);

        }
    }


}
