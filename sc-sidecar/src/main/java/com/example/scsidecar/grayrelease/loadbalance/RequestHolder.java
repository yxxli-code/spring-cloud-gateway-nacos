package com.example.scsidecar.grayrelease.loadbalance;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Create by Brian on 2019/11/18 17:43
 */
public class RequestHolder {
    private static final String REQUEST_KEY = "REQUEST_KEY";
    private static final ConcurrentHashMap<String, ServerHttpRequest> REQUEST_HOLDER = new ConcurrentHashMap<>();

    private RequestHolder() {
    }

    public static void add(ServerHttpRequest request) {
        REQUEST_HOLDER.put(REQUEST_KEY, request);
    }

    public static ServerHttpRequest get() {
        return REQUEST_HOLDER.get(REQUEST_KEY);
    }

    public static void clean() {
        REQUEST_HOLDER.remove(REQUEST_KEY);
    }
}
