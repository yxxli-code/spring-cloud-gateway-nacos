package com.example.scconsumer.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Create by Brian on 2019/11/22 12:24
 */
@Component
@FeignClient(name = "sc-gateway-server")
public interface GatewayRpc {

    @GetMapping("/echo/{string}")
    String echo(@PathVariable("string") String str);
}
