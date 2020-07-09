package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dto.OrderDTO;
import com.tuyrk.sell.service.OrderService;
import com.tuyrk.sell.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class PayServiceImplTest extends ApplicationTests {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Test
    public void create() {
        OrderDTO orderDTO = orderService.findOne("20190112131618398464047");
        payService.create(orderDTO);
    }

    @Test
    public void refund() {
        OrderDTO orderDTO = orderService.findOne("20190112131618398464047");
        payService.create(orderDTO);
    }
}