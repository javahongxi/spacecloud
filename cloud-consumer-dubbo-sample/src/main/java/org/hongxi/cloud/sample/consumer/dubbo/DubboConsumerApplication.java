package org.hongxi.cloud.sample.consumer.dubbo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.hongxi.cloud.sample.api.CloudConstants;
import org.hongxi.cloud.sample.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.apache.dubbo.common.constants.CommonConstants.DubboProperty.DUBBO_PREFER_JSON_FRAMEWORK_NAME;

@SpringBootApplication
public class DubboConsumerApplication {
    private static final Logger log = LoggerFactory.getLogger(DubboConsumerApplication.class);

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        // org.apache.dubbo.common.utils.JsonUtils
        System.setProperty(DUBBO_PREFER_JSON_FRAMEWORK_NAME, CloudConstants.FASTJSON2);
        SpringApplication.run(DubboConsumerApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            String result = demoService.sayHello("lily");
            log.info("invoke dubbo provider, result: {}", result);
        };
    }
}