package com.xinrui.component.strategy.config;

import com.xinrui.component.strategy.core.StrategyBizExecutor;
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

    /**
     * 策略业务执行器
     * @return
     */
    @Bean
    public StrategyBizExecutor strategyBizExecutor() {
        return new StrategyBizExecutor();
    }
}
