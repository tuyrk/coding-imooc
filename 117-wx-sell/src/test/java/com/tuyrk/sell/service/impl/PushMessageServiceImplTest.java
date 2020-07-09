package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dto.OrderDTO;
import com.tuyrk.sell.service.OrderService;
import com.tuyrk.sell.service.PushMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PushMessageServiceImplTest extends ApplicationTests {
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private OrderService orderService;

    @Test
    public void orderStatus() {
        OrderDTO orderDTO = orderService.findOne("20190110114419561487181");
        pushMessageService.orderStatus(orderDTO);
    }
}