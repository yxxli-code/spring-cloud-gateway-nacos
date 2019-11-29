package com.example.scproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ScProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScProductApplication.class, args);
    }

}
