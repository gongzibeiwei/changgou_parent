package com.changgou.consume.service;

import com.changgou.seckill.pojo.SeckillOrder;

/**
 * @Author Jaime
 * @Date 2020/1/29
 * @DESC:
 */
public interface SecKillOrderService {

    /**
     * 将秒杀数据同步到MySQL
     * @param seckillOrder
     * @return
     */
    int createOrder(SeckillOrder seckillOrder);
}
