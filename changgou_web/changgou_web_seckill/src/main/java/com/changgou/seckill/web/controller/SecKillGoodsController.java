package com.changgou.seckill.web.controller;

import com.changgou.entity.Result;
import com.changgou.seckill.feign.SecKillGoodsFeign;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Jaime
 * @Date 2020/1/28
 * @DESC:
 */
@Controller
@RequestMapping("/wseckillgoods")
public class SecKillGoodsController {

    @Autowired
    private SecKillGoodsFeign secKillGoodsFeign;

    /**
     * 跳转秒杀首页
     *
     * @return
     */
    @RequestMapping("/toIndex")
    public String toIndex() {
        return "seckill-index";
    }

    /**
     * 获取秒杀时间段集合信息
     *
     * @return
     */
    @RequestMapping("/timeMenus")
    @ResponseBody
    public List<String> dateMenus() {

        //获取当前时间段相关的信息集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        List<String> result = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }

        return result;

    }

    /**
     * 根据秒杀时间段从redis中查询秒杀商品列表
     *
     * @param time
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result<List<SeckillGoods>> list(String time) {
        return secKillGoodsFeign.list(DateUtil.formatStr(time));
    }
}
