package com.example.scconsumer.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Create by Brian on 2019/11/25 14:28
 */
@Component
@FeignClient(name = "sc-product")
public interface ProductRpc {
    @GetMapping("/app-name/{from}")
    String echoAppName(@PathVariable("from") String from);
}

