package com.example.scgateway;

import com.example.scgateway.route.DynamicRouteServiceImplByNacos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Brian on 12/02/2019
 */
@Component
public class ApplicationStartup implements ApplicationRunner {

    @Autowired
    private DynamicRouteServiceImplByNacos dynamicRouteServiceImplByNacos;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dynamicRouteServiceImplByNacos.dynamicRouteByNacosListener("sc-routes", "DEFAULT_GROUP");
    }
}
