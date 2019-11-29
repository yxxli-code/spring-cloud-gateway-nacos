package com.example.scsidecar.grayrelease.config;

import com.example.scsidecar.grayrelease.loadbalance.GrayLoadBalancerInterceptor;
import com.example.scsidecar.grayrelease.loadbalance.MetadataAwareRule;
import com.example.scsidecar.grayrelease.loadbalance.RequestHolder;
import feign.RequestInterceptor;
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
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            List<ClientHttpRequestInterceptor> list = new ArrayList<>();
            list.add(ribbonInterceptor(loadBalancerClient, requestFactory));
            restTemplate.setInterceptors(list);
        };
    }

    @Bean
    RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            ServerHttpRequest request = RequestHolder.get();
            Optional.ofNullable(request).ifPresent(serverHttpRequest -> {
                Optional.ofNullable(serverHttpRequest.getHeaders()).ifPresent(
                        (headers) -> {
                            log.info("Accessing Version {}", headers.get("VERSION"));
                            requestTemplate.header("VERSION", headers.get("VERSION"));
                        }
                );
            });

        };
    }
}
