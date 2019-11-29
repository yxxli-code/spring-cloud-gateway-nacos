package com.example.scsidecar.grayrelease.config;

import com.example.scsidecar.grayrelease.loadbalance.GrayCachingSpringLoadBalancerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Create by Brian on 2019/11/26 14:30
 */
@Configuration
@AutoConfigureBefore(FeignRibbonClientAutoConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
public class GrayFeignRibbonAutoConfiguration {
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public GrayCachingSpringLoadBalancerFactory cachingLBClientFactory(
            SpringClientFactory factory) {
        return new GrayCachingSpringLoadBalancerFactory(factory);
    }
}
