package com.example.scsidecar.grayrelease.config;

import com.example.scsidecar.grayrelease.loadbalance.RequestHolder;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Create by Brian on 2019/12/6 16:08
 */
@Slf4j
@Configuration
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", matchIfMissing = true)
@ConditionalOnMissingClass("reactor.core.publisher.Mono")
public class ServletFilterAutoConfiguration {

    @Bean
    public FilterRegistrationBean hystrixFilter() {

        FilterRegistrationBean r = new FilterRegistrationBean();
        r.setFilter((request, response, chain) -> {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            LinkedHashMap<String, Map> requestHolder = new LinkedHashMap<>();
            requestHolder.put("HEADERS", new HashMap());
            if (headerNames != null) {
                Map headers = requestHolder.get("HEADERS");
                while (headerNames.hasMoreElements()) {
                    String key = headerNames.nextElement();
                    headers.put(key, httpRequest.getHeader(key));
                }
            }
            RequestHolder.add(requestHolder);

            chain.doFilter(request, response);
        });
        r.addUrlPatterns("/*");
        return r;
    }

    @Bean
    RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            Object request = RequestHolder.get();
            Optional.ofNullable(request).ifPresent(serverHttpRequest -> {
                if(serverHttpRequest instanceof LinkedHashMap) {
                    Map headers = (Map) ((LinkedHashMap) serverHttpRequest).get("HEADERS");
                    Optional.ofNullable(headers).ifPresent(
                            (headMap) -> {
                                log.info("Accessing Version {}", headMap.get("version"));
                                requestTemplate.header("VERSION", (String)headMap.get("version"));
                            }
                    );
                }
            });

        };
    }
}
