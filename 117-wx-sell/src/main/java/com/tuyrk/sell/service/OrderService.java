package com.tuyrk.sell.service;

import com.lly835.bestpay.model.PayResponse;
import com.tuyrk.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 8:58 星期四
 * Description:
 */
public interface OrderService {
    /*创建订单*/
    OrderDTO create(OrderDTO orderDTO);

    /*查询单个订单*/
    OrderDTO findOne(String orderId);

    /*查询订单列表*/
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /*取消订单*/
    OrderDTO cancel(String orderId);

    /*完结订单*/
    OrderDTO finish(String orderId);

    /*支付订单*/
    OrderDTO paid(PayResponse payResponse);

    /*查询订单列表*/
    Page<OrderDTO> findList(Pageable pageable);
}
