package com.example.scsidecar.grayrelease.loadbalance;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Create by Brian on 2019/11/18 17:43
 */
public class RequestHolder {
    private static final String REQUEST_KEY = "REQUEST_KEY";
    private static final ConcurrentHashMap<String, Object> REQUEST_HOLDER = new ConcurrentHashMap<>();

    private RequestHolder() {
    }

    public static void add(Object request) {
        REQUEST_HOLDER.put(REQUEST_KEY, request);
    }

    public static Object get() {
        return REQUEST_HOLDER.get(REQUEST_KEY);
    }

    public static void clean() {
        REQUEST_HOLDER.remove(REQUEST_KEY);
    }
}
