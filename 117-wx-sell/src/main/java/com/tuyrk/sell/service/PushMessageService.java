package com.tuyrk.sell.service;

import com.tuyrk.sell.dto.OrderDTO;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/17 20:00 星期四
 * Description:
 * 推送消息
 */
public interface PushMessageService {
    /**
     * 订单状态变更消息
     */
    void orderStatus(OrderDTO orderDTO);
}
