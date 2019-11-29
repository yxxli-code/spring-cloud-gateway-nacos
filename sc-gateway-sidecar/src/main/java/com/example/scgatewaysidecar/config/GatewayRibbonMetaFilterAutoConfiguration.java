package com.example.scgatewaysidecar.config;

import com.example.scgatewaysidecar.filter.GrayLoadBalancerClientFilter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Create by Brian on 2019/11/18 19:55
 */
@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
public class GatewayRibbonMetaFilterAutoConfiguration {

    @Bean
    public GrayLoadBalancerClientFilter grayLoadBalancerClientFilter(LoadBalancerClient client, LoadBalancerProperties properties) {
        return new GrayLoadBalancerClientFilter(client, properties);
    }
}
