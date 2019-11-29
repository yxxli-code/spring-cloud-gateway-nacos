package com.example.scsidecar.grayrelease.loadbalance;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.example.scsidecar.grayrelease.CommonConstants;
import com.example.scsidecar.grayrelease.config.GrayRibbonRuleProperties;
import com.example.scsidecar.grayrelease.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author Create by Brian on 2019/11/18 17:16
 */
@Slf4j
public class GrayMetadataAwarePredicate extends AbstractDiscoveryEnabledPredicate {
    private final static String DEFAULT_VERSION = "1.0";

    @Override
    protected boolean apply(NacosServer server, LinkedHashMap headers) {
        GrayRibbonRuleProperties ribbonProperties = SpringBeanUtil.getBean(GrayRibbonRuleProperties.class);

        if (!ribbonProperties.isEnabled()) {
            log.debug("gray closed,GrayMetadataAwarePredicate return true");
            return true;
        }

        final Map<String, String> metadata = server.getMetadata();
        String version = metadata.get(CommonConstants.VERSION);
        // 判断Nacos服务是否有版本标签
        if (StringUtils.isBlank(version)) {
            log.info("nacos server tag is blank, GrayMetadataAwarePredicate return false");
            return false;
        }

        // 判断请求中是否有版本
        String target = null;
        if(headers != null) {
            List list = (List)headers.get(CommonConstants.VERSION);
            if(list != null) {
                target = (String) list.get(0);
            }
        }

        if (StringUtils.isBlank(target)) {
            log.info("request headers version is blank, fallback to DEFAULT_VERSION");
            // 默认到缺省版本
            return DEFAULT_VERSION.equals(version);
        }

        log.info("请求版本:{}, 当前服务版本:{}", target, version);
        return target.equals(version);
    }

}
