package com.tuyrk.sell.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.tuyrk.sell.dto.OrderDTO;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/12 14:14 星期六
 * Description:
 * 微信支付
 */
public interface PayService {
    PayResponse create(OrderDTO orderDTO);

    PayResponse notify(String notifyData);

    RefundResponse refund(OrderDTO orderDTO);
}
