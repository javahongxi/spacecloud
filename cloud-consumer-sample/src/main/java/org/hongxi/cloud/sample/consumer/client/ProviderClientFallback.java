package org.hongxi.cloud.sample.consumer.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProviderClientFallback implements ProviderClient {

    @Override
    public String hello(String name) {
        log.warn("Sentinel fallback triggered for provider-sample#hello, name={}", name);
        return "fallback: service unavailable, name=" + name;
    }
}