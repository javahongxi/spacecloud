package org.hongxi.cloud.sample.consumer.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;

/**
 * Created by javahongxi on 2026/6/1.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ReactiveConsumerApplication {
    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
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
