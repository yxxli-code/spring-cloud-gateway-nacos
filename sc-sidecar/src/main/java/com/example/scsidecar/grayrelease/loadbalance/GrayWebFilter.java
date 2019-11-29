package com.example.scsidecar.grayrelease.loadbalance;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Create by Brian on 2019/11/26 15:31
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class GrayWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RequestHolder.add(request);

        return chain.filter(exchange)
                .doFinally(r -> RequestHolder.clean());
    }
}
