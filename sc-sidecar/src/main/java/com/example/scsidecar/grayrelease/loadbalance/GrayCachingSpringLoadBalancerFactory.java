package com.example.scsidecar.grayrelease.loadbalance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;
import org.springframework.cloud.openfeign.ribbon.RetryableFeignLoadBalancer;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * @author Create by Brian on 2019/11/26 14:32
 */
public class GrayCachingSpringLoadBalancerFactory extends CachingSpringLoadBalancerFactory {
    private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

    public GrayCachingSpringLoadBalancerFactory(SpringClientFactory factory) {
        super(factory);
    }

    @Override
    public FeignLoadBalancer create(String clientName) {
        FeignLoadBalancer client = this.cache.get(clientName);
        if(client != null) {
            return client;
        }
        IClientConfig config = this.factory.getClientConfig(clientName);
        ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
        ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);
        client = loadBalancedRetryFactory != null ? new RetryableFeignLoadBalancer(lb, config, serverIntrospector,
                loadBalancedRetryFactory) : new GrayFeignLoadBalancer(lb, config, serverIntrospector);
        this.cache.put(clientName, client);
        return client;
    }
}
