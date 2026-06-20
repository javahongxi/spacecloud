package org.hongxi.cloud.sample.nacos.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by javahongxi on 2026/6/1.
 */
@RefreshScope
@RestController
public class ValueConfigController {

    @Value("${cloud.agent.name}")
    private String agentName;
    @Value("${cloud.agent.credits}")
    private int agentCredits;
    @Value("${cloud.agent.enabled}")
    private boolean agentEnabled;
    @Value("${cloud.agent.provider.model}")
    private String model;

    @GetMapping("/config/value")
    public String agent() {
        return "name:" + agentName + ",credits:" + agentCredits +
                ",enabled:" + agentEnabled + ",model:" + model;
    }
}
