package com.example.scgatewaysidecar.filter;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author Create by Brian on 2019/11/18 16:48
 */
public class GrayLoadBalancerClientFilter extends LoadBalancerClientFilter {
    public GrayLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (this.loadBalancer instanceof RibbonLoadBalancerClient) {
            RibbonLoadBalancerClient client = (RibbonLoadBalancerClient) this.loadBalancer;
            String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.putAll(headers);
            return client.choose(serviceId, linkedHashMap);
        }
        return super.choose(exchange);
    }
}
