package org.hongxi.cloud.sample.provider.reactive.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Slf4j
@RestController
public class DemoController {
    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public Mono<String> hello(String name) {
        log.info("Provider-reactive received request, name: {}", name);
        return Mono.just("Hi, " + name + ", Here is " + port);
    }
}
