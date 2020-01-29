package com.changgou.consume.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.consume.config.RabbitMQConfig;
import com.changgou.consume.service.SecKillOrderService;
import com.changgou.seckill.pojo.SeckillOrder;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author Jaime
 * @Date 2020/1/29
 * @DESC:
 */
@Component
public class ConsumeListener {

    @Autowired
    private SecKillOrderService secKillOrderService;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_ORDER_QUEUE)
    public void receiveSecKillOrderMessage(Message message, Channel channel) {
        //消息预抓取
        try {
            channel.basicQos(300);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //1.转换消息格式
        SeckillOrder seckillOrder = JSON.parseObject(message.getBody(), SeckillOrder.class);

        //基于业务层完成同步MySQL的操作
        int result = secKillOrderService.createOrder(seckillOrder);
        if (result > 0) {
            //同步MySQL成功
            //向消息服务器返回成功通知
            try {
                //参数1：消息的唯一标识；参数2：是否开启消息批处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //同步MySQL失败
            //向消息服务器返回失败通知
            try {
                //参数1：消息的唯一标识；参数2：true代表所有消费者都会拒绝这个消息，false代表只有当前消费者拒绝这个消息；参数3：true代表当前消息进入死信队列（延迟消息队列），false代表当前消息重新进入原有队列中，默认回到头部
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
