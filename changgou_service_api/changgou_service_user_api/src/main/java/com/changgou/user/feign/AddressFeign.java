package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author Jaime
 * @Date 2020/1/17
 * @DESC:
 */
@FeignClient(name = "user")
public interface AddressFeign {
    @GetMapping("/address/list")
    public Result<List<Address>> list();
}
