package org.hongxi.cloud.sample.consumer.reactive;

import org.hongxi.cloud.sample.api.CloudConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;

import static org.apache.dubbo.common.constants.CommonConstants.DubboProperty.DUBBO_PREFER_JSON_FRAMEWORK_NAME;

/**
 * Created by javahongxi on 2026/6/1.
 */
@SpringBootApplication
public class ReactiveConsumerApplication {
    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        // org.apache.dubbo.common.utils.JsonUtils
        System.setProperty(DUBBO_PREFER_JSON_FRAMEWORK_NAME, CloudConstants.FASTJSON2);
        SpringApplication.run(ReactiveConsumerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://provider-reactive-sample")
                .build();
    }
}
