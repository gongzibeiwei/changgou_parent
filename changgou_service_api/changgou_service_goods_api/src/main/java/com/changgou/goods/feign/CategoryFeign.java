package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Jaime
 * @date 2020/1/8
 * @desc
 */
@FeignClient(name = "goods")
public interface CategoryFeign {

    @GetMapping("/category/{id}")
    public Result<Category> findById(@PathVariable("id") Integer id);
}
