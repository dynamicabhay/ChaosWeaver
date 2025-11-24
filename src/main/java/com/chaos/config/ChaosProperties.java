package com.chaos.config;

import ch.qos.logback.core.joran.spi.RuleStore;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.regex.Pattern;

@ConfigurationProperties(prefix = "chaos")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChaosProperties {

    private boolean enabled;

    List<RuleConfig> rules;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class RuleConfig{
        private String name;
        private boolean enabled;
        private MatchConfig match;
        private ActionConfig actions;
        private ChaosMode mode = ChaosMode.AUTO;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class MatchConfig{
        private String pkg;
        private String bean;
        private String method;
        private Pattern beanPattern;
        private Pattern methodPattern;
        private Pattern pkgPattern;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class ActionConfig{
        private LatencyConfig latency;
        private ExceptionConfig exception;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class LatencyConfig{
        private int minMs;
        private int maxMs;
        private double probability;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class ExceptionConfig{
        private String type;
        private String message;
        private double probability;
    }

    @PostConstruct
    public void init(){

        if(rules == null) return;

        for(RuleConfig rule : rules){

            MatchConfig m = rule.getMatch();
            if(m == null) continue;
            if(m.getBean() == null) throw new IllegalArgumentException("chaos.rules.match.bean should be present it cant be null");
            if(m.getPkg() == null) throw new IllegalArgumentException("chaos.rules.match.pkg should be present it cant be null");
            if(m.getMethod() == null) throw new IllegalArgumentException("chaos.rules.match.method should be present it cant be null");

            if(rule.getMatch().getBean() != null)
                m.setBeanPattern(Pattern.compile(m.getBean()));

           if(rule.getMatch().getMethod() != null)
               m.setMethodPattern(Pattern.compile(m.getMethod()));

           if(rule.getMatch().getPkg() != null)
               m.setPkgPattern(Pattern.compile(m.getPkg()));
        }
    }

}
