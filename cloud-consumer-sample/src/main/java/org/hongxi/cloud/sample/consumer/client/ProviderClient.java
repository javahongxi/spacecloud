package org.hongxi.cloud.sample.consumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OpenFeign client for provider-sample,
 * with Sentinel fallback enabled via fallback attribute.
 * Sentinel auto-wraps Feign calls as resources, no @SentinelResource needed.
 *
 * Created by javahongxi on 2026/6/1.
 */
@FeignClient(name = "provider-sample", fallback = ProviderClientFallback.class)
public interface ProviderClient {

    @GetMapping("/hello")
    String hello(@RequestParam("name") String name);
}
