package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dataobject.OrderDetail;
import com.tuyrk.sell.dataobject.OrderMaster;
import com.tuyrk.sell.dto.OrderDTO;
import com.tuyrk.sell.enums.OrderStatusEnum;
import com.tuyrk.sell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class OrderServiceImplTest extends ApplicationTests {

    @Autowired
    private OrderServiceImpl orderService;

    private final static String BUYER_OPENID = "110110";
    private final static String ORDER_ID = "20190110114419561487181";

    @Test
    public void create() {
        //买家信息
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("廖师兄");
        orderDTO.setBuyerAddress("幕课网");
        orderDTO.setBuyerPhone("123456789012");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("1234568");
        o1.setProductQuantity(1);
        orderDetailList.add(o1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123457");
        o2.setProductQuantity(2);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单】result={}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {
        OrderDTO result = orderService.findOne(ORDER_ID);
        log.info("【查询单个订单】result={}", result);
        Assert.assertNotNull(result);
        Assert.assertEquals(ORDER_ID, result.getOrderId());
    }

    @Test
    public void findList() {
        PageRequest request = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID, request);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO.getOrderId());
        Assert.assertNotNull(result);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
        System.out.println(result);
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO.getOrderId());
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
//        OrderDTO result = orderService.paid(orderDTO.getOrderId());
//        System.out.println("result = " + result);
//        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getOrderStatus());
    }

    @Test
    public void copyProperties() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        System.out.println("orderDTO = " + orderDTO);
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123");
        System.out.println("orderMaster = " + orderMaster);
        BeanUtils.copyProperties(orderMaster, orderDTO);
        System.out.println("orderDTO = " + orderDTO);
        //结果证明，orderMaster中没有的属性orderDTO的值不会改变
    }

    @Test
    public void list() {
        PageRequest request = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOPage = orderService.findList(request);
        Assert.assertNotNull(orderDTOPage);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
        Assert.assertTrue("查询所有的订单列表", orderDTOPage.getTotalElements() > 0);
    }
}