package com.example.scsidecar.grayrelease.loadbalance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.openfeign.ribbon.FeignLoadBalancer;

/**
 * @author Create by Brian on 2019/11/26 14:34
 */
public class GrayFeignLoadBalancer extends FeignLoadBalancer {

    public GrayFeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig, ServerIntrospector serverIntrospector) {
        super(lb, clientConfig, serverIntrospector);
    }

    @Override
    protected void customizeLoadBalancerCommandBuilder(final GrayFeignLoadBalancer.RibbonRequest request, final IClientConfig config,
                                                       final LoadBalancerCommand.Builder<GrayFeignLoadBalancer.RibbonResponse> builder) {
        // Inject HTTP Headers
        builder.withServerLocator(request.getRequest().headers());
    }

}
