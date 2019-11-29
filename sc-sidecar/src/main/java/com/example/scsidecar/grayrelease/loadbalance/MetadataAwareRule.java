package com.example.scsidecar.grayrelease.loadbalance;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

/**
 * @author Create by Brian on 2019/11/18 19:54
 */
public class MetadataAwareRule extends ZoneAvoidanceRule {

    @Override
    public AbstractServerPredicate getPredicate() {
        return new GrayMetadataAwarePredicate();
    }
}
