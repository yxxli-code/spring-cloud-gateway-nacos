package com.example.scsidecar.grayrelease.config;

import com.example.scsidecar.grayrelease.loadbalance.GrayLoadBalancerInterceptor;
import com.example.scsidecar.grayrelease.loadbalance.MetadataAwareRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Create by Brian on 2019/11/18 19:55
 */
@Slf4j
@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
public class RibbonMetaFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MetadataAwareRule metadataAwareRule() {
        return new MetadataAwareRule();
    }

    private LoadBalancerInterceptor ribbonInterceptor(
            LoadBalancerClient loadBalancerClient,
            LoadBalancerRequestFactory requestFactory) {
        return new GrayLoadBalancerInterceptor(loadBalancerClient, requestFactory);
    }

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer(LoadBalancerClient loadBalancerClient,
                                                         LoadBalancerRequestFactory requestFactory) {
        return restTemplate -> {
            List<ClientHttpRequestInterceptor> list = restTemplate.getInterceptors();
            list.add(ribbonInterceptor(loadBalancerClient, requestFactory));
            restTemplate.setInterceptors(list);
        };
    }

}
