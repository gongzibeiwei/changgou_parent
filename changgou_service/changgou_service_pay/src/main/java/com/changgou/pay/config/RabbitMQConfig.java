package com.changgou.pay.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Jaime
 * @Date 2020/1/27
 * @DESC:
 */
@Configuration
public class RabbitMQConfig {

    public static final String ORDER_PAY = "order_pay";

    @Bean
    public Queue queue() {
        return new Queue(ORDER_PAY);
    }
}
