package org.hongxi.cloud.sample.nacos.config.controller;

import com.alibaba.cloud.nacos.annotation.NacosConfig;
import com.alibaba.cloud.nacos.annotation.NacosConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Slf4j
@RestController
public class AnnotationConfigController {

    @NacosConfig(dataId = "github.username", group = "DEFAULT_GROUP")
    private String name;

    @GetMapping("/config/hello")
    public String hello() {
        return "Hello, " + name;
    }

    @NacosConfigListener(dataId = "github.username", group = "DEFAULT_GROUP")
    public void updated(String name) {
        log.info("updated: {}", name);
    }
}