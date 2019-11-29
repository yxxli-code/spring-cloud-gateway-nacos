package com.example.scsidecar.grayrelease.loadbalance;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;

/**
 * @author Create by Brian on 2019/11/27 10:22
 */
public class GrayLoadBalancerInterceptor extends LoadBalancerInterceptor {
    private LoadBalancerClient loadBalancer;
    private LoadBalancerRequestFactory requestFactory;

    public GrayLoadBalancerInterceptor(LoadBalancerClient loadBalancer, LoadBalancerRequestFactory requestFactory) {
        super(loadBalancer, requestFactory);
        this.loadBalancer = loadBalancer;
        this.requestFactory = requestFactory;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                        final ClientHttpRequestExecution execution) throws IOException {
        final URI originalUri = request.getURI();
        String serviceName = originalUri.getHost();
        Assert.state(serviceName != null, "Request URI does not contain a valid hostname: " + originalUri);
        if(this.loadBalancer instanceof RibbonLoadBalancerClient) {
            HttpHeaders httpHeaders = request.getHeaders();
            LinkedHashMap hint = new LinkedHashMap();
            hint.putAll(httpHeaders);
            return ((RibbonLoadBalancerClient)this.loadBalancer).execute(serviceName, requestFactory.createRequest(request, body, execution), hint);
        } else {
            return this.loadBalancer.execute(serviceName, requestFactory.createRequest(request, body, execution));
        }
    }
}
