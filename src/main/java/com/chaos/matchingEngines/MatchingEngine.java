package com.chaos.matchingEngines;

import com.chaos.config.ChaosLatencyProperties;
import com.chaos.config.ChaosProperties;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MatchingEngine {


    private final ChaosProperties chaosProps;

    public List<ChaosProperties.RuleConfig> findMatchingRulesForBean(String beanName){
       return this.chaosProps.getRules().stream().filter(
               r -> r.getMatch().getBeanPattern().matcher(beanName).matches()
       ).toList();
    }

    public boolean isMethodIncluded(String methodName){
        return this.chaosProps.getRules().stream()
                .anyMatch(r -> r.getMatch().getMethodPattern().matcher(methodName).matches());
    }

    public boolean isPkgIncluded(String pkgName){

        return this.chaosProps.getRules().stream()
                .anyMatch(r -> r.getMatch().getPkgPattern().matcher(pkgName).matches());
    }
}
