package com.chaos.interceptors;

import com.chaos.config.ChaosLatencyProperties;
import com.chaos.config.ChaosMode;
import com.chaos.config.ChaosProperties;
import com.chaos.matchingEngines.MatchingEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Slf4j
public class ChaosMethodInterceptor implements MethodInterceptor {

    //private final ChaosProperties chaosProps;
    private final MatchingEngine matchingEngine;
    private final List<ChaosProperties.RuleConfig> matchedRules;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        ChaosProperties.RuleConfig matchedRule = matchedRules.get(0);

        if(matchedRule.getMatch().getMethodPattern().matcher(invocation.getMethod().getName()).matches()){
            log.info("method included: " + invocation.getMethod().getName());
            ChaosProperties.ActionConfig actions = matchedRule.getActions();
            ChaosMode mode = matchedRule.getMode();

            switch(mode){

                case LATENCY_ONLY -> {
                    maybeLatency(actions.getLatency(),invocation);
                    return invocation.proceed();
                }

                case EXCEPTION_ONLY -> {
                    maybeException(actions.getException(),invocation);
                    return invocation.proceed();
                }

                case AUTO -> {
                    handleAuto(actions,invocation);
                    return invocation.proceed();
                }

                default -> {
                    handleAuto(actions,invocation);
                }


            }

        }
        return invocation.proceed();
    }


    private void maybeLatency(ChaosProperties.LatencyConfig latencyConfig,MethodInvocation invocation){

        if(latencyConfig == null) return;

        int minMs = latencyConfig.getMinMs();
        int maxMs = latencyConfig.getMaxMs();
        int value = ThreadLocalRandom.current().nextInt(minMs, maxMs + 1);
        double rand = Math.random();
        log.info("event=injectingChaosLatency injecting latency to method {}, latency : {}",invocation.getMethod().getName(),value);

        try {
            if (rand <= latencyConfig.getProbability()) {
                Thread.sleep(value);
                log.info("latency of {} is injected to method: {}", value,invocation.getMethod().getName());
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void maybeException(ChaosProperties.ExceptionConfig exceptionConfig,MethodInvocation invocation) throws Throwable{
        if(exceptionConfig == null) return;
        log.info("event=injectingChaosException injecting Exception to method {}, type: {} ",invocation.getMethod().getName(),exceptionConfig.getType());


        if(Math.random() > exceptionConfig.getProbability()) return;

        throw buildException(exceptionConfig);

    }

    private void handleAuto(ChaosProperties.ActionConfig actions,MethodInvocation invocation) throws Throwable{
        log.info("event=handlingAutoModeChaosInjection for method : {}",invocation.getMethod().getName());
        if(actions.getLatency() != null) maybeLatency(actions.getLatency(),invocation);
        if(actions.getException() != null) maybeException(actions.getException(),invocation);
    }

    private Throwable buildException(ChaosProperties.ExceptionConfig exConfig) {
        try {
            Class<?> clazz = Class.forName(exConfig.getType());

            // Try constructor with (String message)
            Constructor<?> ctor = clazz.getConstructor(String.class);
            return (Throwable) ctor.newInstance(exConfig.getMessage());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Exception class not found: " + exConfig.getType(), e);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    "Exception class must have a (String) constructor: " + exConfig.getType(), e);

        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate exception: " + exConfig.getType(), e);
        }
    }
}
