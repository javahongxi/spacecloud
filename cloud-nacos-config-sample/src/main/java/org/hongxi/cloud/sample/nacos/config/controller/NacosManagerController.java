package org.hongxi.cloud.sample.nacos.config.controller;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Slf4j
@RestController
@RequestMapping("/nacos")
public class NacosManagerController {

    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private NacosConfigManager nacosConfigManager;

    /**
     * Get configuration.
     *
     * @param dataId dataId
     * @param group group
     * @return config
     */
    @RequestMapping("/getConfig")
    public String getConfig(@RequestParam("dataId") String dataId,
                            @RequestParam(value = "group", required = false, defaultValue = DEFAULT_GROUP) String group)
            throws NacosException {
        ConfigService configService = nacosConfigManager.getConfigService();
        return configService.getConfig(dataId, group, 2000);
    }

    /**
     * Publish configuration.
     *
     * @param dataId dataId
     * @param group group
     * @param content content
     * @return boolean
     */
    @RequestMapping("/publishConfig")
    public boolean publishConfig(@RequestParam("dataId") String dataId,
                                 @RequestParam(value = "group", required = false, defaultValue = DEFAULT_GROUP) String group,
                                 @RequestParam("content") String content) throws NacosException {
        ConfigService configService = nacosConfigManager.getConfigService();
        return configService.publishConfig(dataId, group, content);
    }

    /**
     * Delete configuration.
     *
     * @param dataId dataId
     * @param group group
     * @return boolean
     */
    @RequestMapping("/removeConfig")
    public boolean removeConfig(@RequestParam("dataId") String dataId,
                                @RequestParam(value = "group", required = false, defaultValue = DEFAULT_GROUP) String group)
            throws NacosException {
        ConfigService configService = nacosConfigManager.getConfigService();
        return configService.removeConfig(dataId, group);
    }

    /**
     * Add listener for configuration.
     *
     * @param dataId dataId
     * @param group group
     */
    @RequestMapping("/listener")
    public String listenerConfig(@RequestParam("dataId") String dataId,
                                 @RequestParam(value = "group", required = false, defaultValue = DEFAULT_GROUP) String group)
            throws NacosException {
        ConfigService configService = nacosConfigManager.getConfigService();
        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return executorService;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("[Listen for configuration changes]: {} {}", dataId, configInfo);
            }
        });
        return "Add Lister successfully!";
    }
}
