package com.chaos.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "chaos")
@Validated
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChaosLatencyProperties {

    private boolean enabled;
    private LatencyConfig latency = new LatencyConfig();

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class LatencyConfig {

        private boolean enabled;

        private int minMs;
        private int maxMs;

        private double probability = 1.0;

        private boolean log;

        private TargetConfig targets = new TargetConfig();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class TargetConfig{
        private List<String> beanNamePatterns;
        private List<String> packagePatterns;
        private List<MethodConfig> methods;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class MethodConfig{
        private String name;
        private int minMs;
        private int maxMs;
        private double probability;
    }


}
