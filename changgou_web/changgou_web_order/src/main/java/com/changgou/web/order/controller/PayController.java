package com.changgou.web.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.PayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/wxpay")
public class PayController {
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private PayFeign payFeign;

    /**
     * 跳转到微信支付二维码页面
     *
     * @param orderId
     * @return
     */
    @GetMapping
    public String wxPay(String orderId, Model model) {
        //1.根据orderId查询订单信息，如果订单不存在，跳转到错误页面
        Result<Order> orderResult = orderFeign.findById(orderId);
        if (orderResult.getData() == null) {
            return "fail";
        }
        //2.根据订单支付状态进行判断，如果不是未支付的订单，则跳转到错误页面
        Order order = orderResult.getData();
        if (!"0".equals(order.getPayStatus())) {
            return "fail";
        }
        //3.基于payFeign调用统一下单接口并获取返回结果
        Result payResult = payFeign.nativePay(orderId, order.getPayMoney());
        if (payResult.getData() == null) {
            return "fail";
        }
        //4.封装结果数据
        Map payMap = (Map) payResult.getData();
        payMap.put("orderId", orderId);
        payMap.put("payMoney", order.getPayMoney());

        model.addAllAttributes(payMap);
        return "wxpay";
    }
}
