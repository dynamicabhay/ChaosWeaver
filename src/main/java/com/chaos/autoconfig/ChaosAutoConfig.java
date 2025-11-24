package com.chaos.autoconfig;

import com.chaos.config.ChaosLatencyProperties;
import com.chaos.config.ChaosProperties;
import com.chaos.interceptors.ChaosMethodInterceptor;
import com.chaos.matchingEngines.MatchingEngine;
import com.chaos.processors.ChaosBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ChaosProperties.class)
@ConditionalOnProperty(prefix = "chaos", name = "enabled", havingValue = "true",matchIfMissing = false)
public class ChaosAutoConfig {

    @Bean
   // @ConditionalOnProperty(prefix = "chaos", name = "enabled", havingValue = "true",matchIfMissing = false)
    public static ChaosBeanPostProcessor chaosBeanPostProcessor(ChaosProperties props,MatchingEngine matchingEngine){
        return new ChaosBeanPostProcessor(props,matchingEngine);
    }

    @Bean
   // @ConditionalOnProperty(prefix = "chaos", name = "enabled", havingValue = "true",matchIfMissing = false)
    public static MatchingEngine matchingEngine(ChaosProperties props){
        return new MatchingEngine(props);
    }



}
