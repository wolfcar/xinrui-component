package com.xinrui.component.swak.config;

import com.xinrui.component.swak.core.LookupSwakBiz;
import com.xinrui.component.swak.core.SwakBizExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b><code>SwakConfiguration</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/25 23:06.
 *
 * @author jerry
 * @since guoke-component
 */
@Configuration
public class SwakConfiguration {

    @Bean
    LookupSwakBiz lookupSwakBiz() {
        return new LookupSwakBiz();
    }

    @Bean
    SwakBizExecutor swakBizExecutor() {
        return new SwakBizExecutor();
    }
}
