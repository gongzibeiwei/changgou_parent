package com.changgou.seckill.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Jaime
 * @Date 2020/1/28
 * @DESC:
 */
@RestController
@RequestMapping("/seckillgoods")
public class SecKillGoodsController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    /**
     * 根据秒杀时间段查询秒杀商品列表
     *
     * @param time
     * @return
     */
    @RequestMapping("/list")
    public Result<List<SeckillGoods>> list(@RequestParam("time") String time) {
        List<SeckillGoods> seckillGoodsList = seckillGoodsService.list(time);
        return new Result<>(true, StatusCode.OK, "查询成功", seckillGoodsList);
    }
}
