package com.xinrui.component.strategy.config;

import com.xinrui.component.strategy.core.StrategyBizExecutor;
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
public class StrategyConfiguration {

    @Bean
    public StrategyBizExecutor strategyBizExecutor() {
        return new StrategyBizExecutor();
    }
}
