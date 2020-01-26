package com.changgou.pay.service.impl;

import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayServiceImpl implements WXPayService {
    @Autowired
    private WXPay wxPay;

    /**
     * native统一下单支付调用接口
     *
     * @param orderId
     * @param money
     * @return
     */
    @Override
    public Map nativePay(String orderId, Integer money) {
        try {
            //1.封装请求参数
            Map<String, String> map = new HashMap<>();
            map.put("body", "畅购");//商品描述
            map.put("out_trade_no", orderId);//商户订单号

            //基于测试，支付金额进行固定
            BigDecimal payMoney = new BigDecimal("0.01");//单位：元
            BigDecimal fen = payMoney.multiply(new BigDecimal("100"));//1.00分
            fen = fen.setScale(0, BigDecimal.ROUND_UP);//1

            map.put("total_fee", String.valueOf(fen));//标价金额
            map.put("spbill_create_ip", "127.0.0.1");//终端IP
            map.put("notify_url", "http://www.itcast.cn");//通知地址
            map.put("trade_type", "NATIVE");//交易类型
            //2.基于wxpay完成统一下单接口调用，并获取返回结果
            Map<String, String> result = wxPay.unifiedOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
