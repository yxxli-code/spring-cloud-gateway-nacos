package com.example.scsidecar.grayrelease.config;

import com.example.scsidecar.grayrelease.loadbalance.RequestHolder;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Optional;

/**
 * @author Create by Brian on 2019/12/6 16:10
 */
@Slf4j
@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnClass(reactor.core.publisher.Mono.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
public class WebfluxFilterAutoConfiguration {

    @Bean
    RequestInterceptor webfluxRequestTokenBearerInterceptor() {
        return requestTemplate -> {
            Object request = RequestHolder.get();
            Optional.ofNullable(request).ifPresent(serverHttpRequest -> {
                if(serverHttpRequest instanceof ServerHttpRequest) {
                    HttpHeaders headers = ((ServerHttpRequest) serverHttpRequest).getHeaders();
                    Optional.ofNullable(headers).ifPresent(
                            (headMap) -> {
                                log.info("Reactive Accessing Version {}", headMap.get("version"));
                                requestTemplate.header("VERSION", headMap.get("version"));
                            }
                    );
                }
            });

        };
    }
}
