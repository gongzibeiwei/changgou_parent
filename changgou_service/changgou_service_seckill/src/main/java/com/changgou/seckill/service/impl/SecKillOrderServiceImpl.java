package com.changgou.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.config.ConfirmMessageSender;
import com.changgou.seckill.config.RabbitMQConfig;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SecKillOrderService;
import com.changgou.util.IdWorker;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jaime
 * @Date 2020/1/29
 * @DESC:
 */
@Service
public class SecKillOrderServiceImpl implements SecKillOrderService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ConfirmMessageSender confirmMessageSender;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    public static final String SECKILL_GOODS_KEY = "seckill_goods_";
    public static final String SECKILL_GOODS_STOCK_COUNT_KEY = "seckill_goods_stock_count_";

    /**
     * 秒杀下单
     *
     * @param id
     * @param time
     * @param username
     * @return
     */
    @Override
    public boolean add(Long id, String time, String username) {

        //防止用户恶意访问刷单
        String commit = this.preventRepeatCommit(username, id);
        if ("fail".equals(commit)){
            return false;
        }
        //防止相同商品重复购买
        SeckillOrder seckillOrderResult = seckillOrderMapper.getOrderInfoByUsernameAndGoodsId(username, id);
        if (seckillOrderResult != null){
            return false;
        }

        //1.获取redis中的商品信息与库存信息，并进行判断
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SECKILL_GOODS_KEY + time).get("id");
        String redisStock = (String) redisTemplate.opsForValue().get(SECKILL_GOODS_STOCK_COUNT_KEY + id);
        if (StringUtils.isEmpty(redisStock)) {
            return false;
        }
        int stock = Integer.parseInt(redisStock);//库存
        if (seckillGoods == null || stock <= 0) {//商品为空或库存小于等于0
            return false;
        }
        //2.执行redis的预扣减库存操作，并获取扣减之后的库存值
        //decrement：减1
        //increment：加
        Long decrement = redisTemplate.opsForValue().decrement(SECKILL_GOODS_STOCK_COUNT_KEY + id);
        if (decrement <= 0) {
            //3.如果扣减之后的库存值<=0,则删除redis中响应的商品信息与库存信息
            redisTemplate.boundHashOps(SECKILL_GOODS_KEY + time).delete(id);
            redisTemplate.delete(SECKILL_GOODS_STOCK_COUNT_KEY + id);
        }
        //4.基于MQ完成MySQL的数据同步，并进行异步下单并扣减库存（MySQL）
        //发送消息（保证消息生产者对于消息的不丢失实现）
        //消息体：秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(id);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");

        //发送消息
        confirmMessageSender.sendMessage("", RabbitMQConfig.SECKILL_ORDER_QUEUE, JSON.toJSONString(seckillOrder));

        return true;
    }

    /**
     * 防止用户恶意刷单
     *
     * @param username
     * @param id
     * @return
     */
    private String preventRepeatCommit(String username, Long id) {
        String redis_key = "seckill_user_" + username + "_id_" + id;
        Long count = redisTemplate.opsForValue().increment(redis_key, 1);
        if (count == 1) {
            //代表当前用户是第一次访问
            //对当前的key设置一个五分钟的有效期
            redisTemplate.expire(redis_key, 5, TimeUnit.MINUTES);
            return "success";
        }
        if (count > 1) {
            return "fail";
        }
        return "fail";
    }
}
