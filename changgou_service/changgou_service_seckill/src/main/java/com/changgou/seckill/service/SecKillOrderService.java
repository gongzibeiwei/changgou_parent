package com.changgou.seckill.service;


/**
 * @Author Jaime
 * @Date 2020/1/29
 * @DESC:
 */
public interface SecKillOrderService {

    /**
     * 秒杀下单
     *
     * @param id
     * @param time
     * @param username
     * @return
     */
    boolean add(Long id, String time, String username);


}
