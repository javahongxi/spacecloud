package org.hongxi.cloud.sample.stream;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Configuration
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    /**
     * 函数式消费者，自动绑定到 input-in-0 binding
     */
    @Bean
    public Consumer<String> input() {
        return message -> log.info("Received message: {}", message);
    }
}
