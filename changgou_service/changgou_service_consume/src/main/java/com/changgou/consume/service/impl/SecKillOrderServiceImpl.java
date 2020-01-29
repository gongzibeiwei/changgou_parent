package com.changgou.consume.service.impl;

import com.changgou.consume.dao.SeckillGoodsMapper;
import com.changgou.consume.dao.SeckillOrderMapper;
import com.changgou.consume.service.SecKillOrderService;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Jaime
 * @Date 2020/1/29
 * @DESC:
 */
public class SecKillOrderServiceImpl implements SecKillOrderService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    /**
     * 将秒杀数据同步到MySQL
     *
     * @param seckillOrder
     * @return
     */
    @Override
    @Transactional
    public int createOrder(SeckillOrder seckillOrder) {
        //1.扣减秒杀商品库存
        int result = seckillGoodsMapper.updateStockCount(seckillOrder.getSeckillId());
        if (result <= 0) {
            return 0;
        }
        //2.新增秒杀订单
        result = seckillOrderMapper.insertSelective(seckillOrder);
        if (result <= 0){
            return 0;
        }
        return 1;
    }
}
