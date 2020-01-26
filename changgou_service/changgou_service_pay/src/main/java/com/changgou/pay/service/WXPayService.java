package com.changgou.pay.service;

import java.util.Map;

public interface WXPayService {
    /**
     * native统一下单支付调用接口
     *
     * @param orderId
     * @param money
     * @return
     */
    Map nativePay(String orderId, Integer money);
}
