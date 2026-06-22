package org.hongxi.cloud.sample.consumer.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProviderClientFallback implements ProviderClient {

    private static final Logger log = LoggerFactory.getLogger(ProviderClientFallback.class);

    @Override
    public String hello(String name) {
        log.warn("Sentinel fallback triggered for provider-sample#hello, name={}", name);
        return "fallback: service unavailable, name=" + name;
    }
}