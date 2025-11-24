package com.chaos.config;

import ch.qos.logback.core.joran.spi.RuleStore;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
        private ChaosMode mode;
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

        for(RuleConfig rule : rules){
            MatchConfig m = rule.getMatch();
            if(rule.getMatch().getBean() != null)
                m.setBeanPattern(Pattern.compile(m.getBean()));

           if(rule.getMatch().getPkg() != null)
               m.setMethodPattern(Pattern.compile(m.getMethod()));

           if(rule.getMatch().getPkg() != null)
               m.setPkgPattern(Pattern.compile(m.getPkg()));
        }
    }

}
