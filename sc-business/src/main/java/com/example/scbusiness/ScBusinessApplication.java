package com.example.scbusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.scsidecar", "com.example"})
public class ScBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScBusinessApplication.class, args);
    }

}
