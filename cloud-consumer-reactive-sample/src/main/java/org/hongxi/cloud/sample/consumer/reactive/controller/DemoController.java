package org.hongxi.cloud.sample.consumer.reactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hongxi.cloud.sample.api.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Slf4j
@RestController
public class DemoController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ReactiveDiscoveryClient reactiveDiscoveryClient;

    @DubboReference(check = false)
    private DemoService demoService;

    @RequestMapping("/hi")
    public Mono<String> hi(String name, @RequestHeader(value = "traceparent", required = false) String traceparent) {
        log.info("traceparent: {}", traceparent);
        log.info("Consumer calling provider via WebClient, name: {}", name);
        return webClient
                .get()
                .uri("/hello?name={name}", name)
                .headers(h -> {
                    if (traceparent != null) {
                        h.set("traceparent", traceparent);
                    }
                })
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/services")
    public Flux<String> allServices() {
        return reactiveDiscoveryClient.getInstances("provider-reactive-sample")
                .map(serviceInstance -> serviceInstance.getHost() + ":"
                        + serviceInstance.getPort());
    }

    @RequestMapping("/dubbo")
    public Mono<String> sayHello(String name, @RequestHeader(value = "traceparent", required = false) String traceparent) {
        log.info("traceparent: {}", traceparent);
        log.info("Consumer calling provider via Dubbo Reference, name: {}", name);
        return Mono.fromFuture(demoService.sayHelloAsync(name));
    }
}
