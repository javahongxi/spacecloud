package org.hongxi.cloud.sample.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Slf4j
@RestController
public class DemoController {
    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String hello(String name) {
        log.info("Provider received request, name: {}", name);
        return "Hi, " + name + ", Here is " + port;
    }
}
