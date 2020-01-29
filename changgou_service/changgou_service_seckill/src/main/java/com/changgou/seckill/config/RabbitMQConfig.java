package com.changgou.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Jaime
 * @Date 2020/1/29
 * @DESC:
 */
@Configuration
public class RabbitMQConfig {
    public static final String SECKILL_ORDER_QUEUE = "seckill_order";

    @Bean
    public Queue queue(){
        return new Queue(SECKILL_ORDER_QUEUE,true);
    }
}
