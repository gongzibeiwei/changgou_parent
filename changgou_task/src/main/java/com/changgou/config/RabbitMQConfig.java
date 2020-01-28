package com.changgou.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Jaime
 * @Date 2020/1/28
 * @DESC:
 */
@Configuration
public class RabbitMQConfig {
    public static final String ORDER_TACK = "order_tack";

    @Bean
    public Queue queue(){
        return new Queue(ORDER_TACK);
    }
}
