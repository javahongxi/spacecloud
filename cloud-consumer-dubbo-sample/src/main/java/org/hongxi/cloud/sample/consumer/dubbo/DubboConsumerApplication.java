package org.hongxi.cloud.sample.consumer.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hongxi.cloud.sample.api.DemoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class DubboConsumerApplication {

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
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