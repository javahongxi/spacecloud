package org.hongxi.cloud.sample.nacos.config.controller;

import org.hongxi.cloud.sample.nacos.config.properties.AgentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by javahongxi on 2026/6/1.
 */
@RestController
public class BeanConfigController {

    @Autowired
    private AgentProperties agentProperties;

    @GetMapping("/config/agent")
    public AgentProperties agent() {
        return agentProperties;
    }
}
