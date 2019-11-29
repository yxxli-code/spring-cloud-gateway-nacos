package com.example.scsidecar.grayrelease.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Create by Brian on 2019/11/18 17:30
 */
@Data
@Component
@Configuration
@ConfigurationProperties("ribbon.filter.metadata")
public class GrayRibbonRuleProperties {
    private boolean enabled;

}
