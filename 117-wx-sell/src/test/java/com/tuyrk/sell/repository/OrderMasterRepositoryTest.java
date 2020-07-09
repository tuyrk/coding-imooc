package com.tuyrk.sell.repository;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class OrderMasterRepositoryTest extends ApplicationTests {

    @Autowired
    private OrderMasterRepository repository;

    private static final String OPENID = "110110";

    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123456");
        orderMaster.setBuyerName("师兄");
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setBuyerAddress("幕课网");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(2.5));

        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest request = PageRequest.of(0, 3);
        Page<OrderMaster> result = repository.findByBuyerOpenid(OPENID, request);
//        System.out.println(result.getTotalPages());
//        System.out.println(result.getTotalElements());
        Assert.assertNotEquals(0, result.getTotalElements());
    }
}