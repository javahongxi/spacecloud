package org.hongxi.cloud.sample.consumer;

import feign.RequestInterceptor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Created by javahongxi on 2026/6/1.
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RequestInterceptor feignTracingInterceptor(Tracer tracer) {
        return template -> {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                String traceparent = String.format("00-%s-%s-01",
                        currentSpan.context().traceId(),
                        currentSpan.context().spanId());
                template.header("traceparent", traceparent);
            }
        };
    }
}
