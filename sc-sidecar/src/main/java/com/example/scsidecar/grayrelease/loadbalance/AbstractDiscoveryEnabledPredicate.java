package com.example.scsidecar.grayrelease.loadbalance;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import org.springframework.lang.Nullable;

import java.util.LinkedHashMap;

/**
 * @author Create by Brian on 2019/11/18 17:12
 */
public abstract class AbstractDiscoveryEnabledPredicate extends AbstractServerPredicate {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean apply(@Nullable PredicateKey input) {
        return input != null
                && input.getServer() instanceof NacosServer
                && input.getLoadBalancerKey() instanceof LinkedHashMap
                && apply((NacosServer) input.getServer(), (LinkedHashMap) input.getLoadBalancerKey());
    }

    abstract protected boolean apply(NacosServer server, LinkedHashMap headers);
}
