package org.hongxi.cloud.sample.provider.reactive.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by javahongxi on 2026/6/1.
 */
@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public Mono<String> hello(String name) {
        log.info("Provider-reactive received request, name: {}", name);
        return Mono.just("Hi, " + name + ", Here is " + port);
    }
}
