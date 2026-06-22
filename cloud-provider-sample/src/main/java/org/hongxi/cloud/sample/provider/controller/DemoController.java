package org.hongxi.cloud.sample.provider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by javahongxi on 2026/6/1.
 */
@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String hello(String name) {
        log.info("Provider received request, name: {}", name);
        return "Hi, " + name + ", Here is " + port;
    }
}
