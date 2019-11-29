package com.example.scgateway.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class DynamicRouteServiceImplByNacos {

    @Value("${spring.cloud.nacos.discovery.server-addr:127.0.0.1:8848}")
    private String nacosServer;

    private DynamicRouteServiceImpl dynamicRouteService;

    @Autowired
    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    /**
     * 监听Nacos Server下发的动态路由配置
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener (String dataId, String group){
        try {
            ConfigService configService=NacosFactory.createConfigService(nacosServer);
            String content = configService.getConfig(dataId, group, 5000);
            log.info("Loading routes: {}", content);
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    List<RouteDefinition> definitions = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("Receiving routes ：{}", definitions);
                    for(RouteDefinition definition: definitions) {
                        dynamicRouteService.update(definition);
                    }
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            //todo 提醒:异常自行处理此处省略
        }
    }

}
