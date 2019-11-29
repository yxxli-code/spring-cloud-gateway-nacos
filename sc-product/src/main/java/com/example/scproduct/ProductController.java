package com.example.scproduct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Create by Brian on 2019/11/18 13:25
 */
@RestController
@RefreshScope
public class ProductController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${custom.test-value:none}")
    private String testValue;

    @GetMapping("/app-name/{from}")
    public String echoAppName(@PathVariable String from) {
        return "from "+ from + "#product@" + serverPort;
    }

    @GetMapping("/get/test-value")
    public String getTestValue() {
        return testValue + "@" + serverPort;
    }
}
