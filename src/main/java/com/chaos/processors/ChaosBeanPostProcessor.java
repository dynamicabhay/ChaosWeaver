package com.chaos.processors;

import com.chaos.config.ChaosLatencyProperties;
import com.chaos.config.ChaosProperties;
import com.chaos.interceptors.ChaosMethodInterceptor;
import com.chaos.matchingEngines.MatchingEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

import java.util.List;

@RequiredArgsConstructor
public class ChaosBeanPostProcessor implements BeanPostProcessor {

     private final ChaosProperties chaosProps;
     private final MatchingEngine matchingEngine;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


       if(!chaosProps.isEnabled() || !matchingEngine.isPkgIncluded(ClassUtils.getPackageName(bean.getClass()))
       ){
           return bean;
       }

       List<ChaosProperties.RuleConfig> matchedRules = matchingEngine.findMatchingRulesForBean(bean.getClass().getName());

       if(matchedRules.isEmpty()) return bean;

       // now we got the bean for which we need to create proxy bean
        ProxyFactory factory = new ProxyFactory(bean);

        factory.addAdvice(new ChaosMethodInterceptor(matchingEngine,matchedRules));
        return factory.getProxy();



    }
}
