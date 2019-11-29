package com.example.scconsumer;

import com.example.scconsumer.rpc.GatewayRpc;
import com.example.scconsumer.rpc.ProductRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Create by Brian on 2019/11/18 13:25
 */
@RestController
@RefreshScope
public class NacosController {

    private final GatewayRpc gatewayRpc;

    private final ProductRpc productRpc;

    private final RestTemplate restTemplate;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${custom.test-value:none}")
    private String testValue;

    @Autowired
    public NacosController(GatewayRpc gatewayRpc, ProductRpc productRpc, RestTemplate restTemplate) {
        this.gatewayRpc = gatewayRpc;
        this.productRpc = productRpc;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/echo/app-name")
    public String echoAppName() {
        String echo = restTemplate.getForObject("http://sc-gateway-server/echo/" + appName, String.class);
        return echo + "@" + serverPort;
    }

    @GetMapping("/get/gateway")
    public String getGateway() {
        return gatewayRpc.echo("gateway") + "@" + serverPort;
    }

    @GetMapping("/get/product")
    public String getProduct() {
        return productRpc.echoAppName(serverPort);
    }

    @GetMapping("/get/test-value")
    public String getTestValue() {
        return testValue + "@" + serverPort;
    }
}
