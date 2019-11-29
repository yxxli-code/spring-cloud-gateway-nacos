package com.example.scgateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Create by Brian on 2019/11/18 16:47
 */
@RestController
public class EchoController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        return "Hello Nacos Discovery " + string + "@" + serverPort;
    }
}
