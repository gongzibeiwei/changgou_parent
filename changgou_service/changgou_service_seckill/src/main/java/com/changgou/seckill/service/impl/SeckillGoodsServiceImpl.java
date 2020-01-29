package com.changgou.seckill.service.impl;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @Author Jaime
 * @Date 2020/1/28
 * @DESC:
 */
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String SECKILL_GOODS_KEY = "seckill_goods_";
    public static final String SECKILL_GOODS_STOCK_COUNT_KEY = "seckill_goods_stock_count_";

    /**
     * 根据秒杀时间段从redis中查询秒杀商品列表
     *
     * @param time
     * @return
     */
    @Override
    public List<SeckillGoods> list(String time) {
        List<SeckillGoods> list = redisTemplate.boundHashOps(SECKILL_GOODS_KEY + time).values();

        //更新库存数据来源
        for (SeckillGoods seckillGoods : list) {
            String value = (String) redisTemplate.opsForValue().get(SECKILL_GOODS_STOCK_COUNT_KEY+seckillGoods.getId());
            seckillGoods.setStockCount(Integer.parseInt(value));
        }
        return list;
    }
}
