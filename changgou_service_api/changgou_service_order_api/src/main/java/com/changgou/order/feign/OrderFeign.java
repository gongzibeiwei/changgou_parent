package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Jaime
 * @Date 2020/1/17
 * @DESC:
 */
@FeignClient(name = "order")
public interface OrderFeign {
    @PostMapping("/order")
    public Result add(@RequestBody Order order);
}
