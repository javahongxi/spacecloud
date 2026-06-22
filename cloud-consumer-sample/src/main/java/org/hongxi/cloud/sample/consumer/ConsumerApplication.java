package org.hongxi.cloud.sample.consumer;

import org.hongxi.cloud.sample.api.CloudConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.apache.dubbo.common.constants.CommonConstants.DubboProperty.DUBBO_PREFER_JSON_FRAMEWORK_NAME;

/**
 * Created by javahongxi on 2026/6/1.
 */
@SpringBootApplication
@EnableFeignClients
public class ConsumerApplication {
    public static void main(String[] args) {
        // org.apache.dubbo.common.utils.JsonUtils
        System.setProperty(DUBBO_PREFER_JSON_FRAMEWORK_NAME, CloudConstants.FASTJSON2);
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
