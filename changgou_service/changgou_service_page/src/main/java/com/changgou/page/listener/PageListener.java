package com.changgou.page.listener;

import com.changgou.page.config.RabbitMQConfig;
import com.changgou.page.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jaime
 * @date 2020/1/8
 * @desc
 */
@Component
public class PageListener {
    @Autowired
    private PageService pageService;

    @RabbitListener(queues = RabbitMQConfig.PAGE_CREATE_QUEUE)
    public void receiveMessage(String spuId) {
        System.out.println("获取静态化页面的商品id：" + spuId);
        //调用业务层方法生成静态化页面
        pageService.generateHtml(spuId);
    }
}
