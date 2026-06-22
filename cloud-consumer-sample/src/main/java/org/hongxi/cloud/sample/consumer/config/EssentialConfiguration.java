package org.hongxi.cloud.sample.consumer.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import feign.RequestInterceptor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.client.RestTemplate;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Configuration(proxyBeanMethods = false)
public class EssentialConfiguration {

    private static final Logger log = LoggerFactory.getLogger(EssentialConfiguration.class);

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class)
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

    static class ExceptionUtil {

        private ExceptionUtil() {
        }

        public static SentinelClientHttpResponse handleException(
                HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException e) {
            log.info("Oops: {}", e.getClass().getCanonicalName());
            return new SentinelClientHttpResponse("Blocked by Sentinel");
        }
    }
}