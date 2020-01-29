package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillGoods;

import java.util.List;

/**
 * @Author Jaime
 * @Date 2020/1/28
 * @DESC:
 */
public interface SeckillGoodsService {

    /**
     * 根据秒杀时间段从redis中查询秒杀商品列表
     *
     * @param time
     * @return
     */
    List<SeckillGoods> list(String time);
}
