package com.changgou.task;

import com.changgou.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Jaime
 * @Date 2020/1/28
 * @DESC:
 */
@Component
public class OrderTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 定时任务，每天执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoTask() {
        System.out.println(new Date());
        rabbitTemplate.convertAndSend("", RabbitMQConfig.ORDER_TACK, "-");
    }
}
