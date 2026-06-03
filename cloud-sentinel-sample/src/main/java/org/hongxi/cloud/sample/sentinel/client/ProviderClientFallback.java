package org.hongxi.cloud.sample.sentinel.client;

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

    @Override
    public String echo(String message) {
        log.warn("Sentinel fallback triggered for provider-sample#echo, message={}", message);
        return "fallback: service unavailable, message=" + message;
    }
}
