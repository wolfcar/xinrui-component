package com.xinrui.component.common.spring;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b><code>AbstractCandicateRegistrar</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/24 22:06.
 *
 * @author jerry
 * @since guoke-component
 */
public abstract class AbstractCandidateComponentRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanClassLoaderAware, ResourceLoaderAware {

    protected Environment environment;


    protected ClassLoader classLoader;

    protected ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();

        List<AnnotationTypeFilter> annotationTypeFilters = new ArrayList<>();
        generateFilters(annotationTypeFilters);

        assert !CollectionUtils.isEmpty(annotationTypeFilters);
        annotationTypeFilters.forEach(annotationTypeFilter -> scanner.addIncludeFilter(annotationTypeFilter));

        Set<String> basePackages = getBasePackages(importingClassMetadata);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                if (candidate instanceof AnnotatedBeanDefinition) {
                    registerBeanDefinition(registry, importBeanNameGenerator, candidate);
                }
            }
        }
    }

    protected abstract void registerBeanDefinition(BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator, BeanDefinition candidate);

    protected abstract void generateFilters(List<AnnotationTypeFilter> annotationTypeFilters);

    protected abstract Set<String> getBasePackages(AnnotationMetadata importingClassMetadata);


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Set the {@code Environment} that this component runs in.
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    protected ClassPathScanningCandidateComponentProvider getScanner() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface()) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(),
                                    classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error("Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);
                        }
                    }
                }
                return false;
            }
        };
        scanner.setResourceLoader(this.resourceLoader);
        return scanner;
    }


}
