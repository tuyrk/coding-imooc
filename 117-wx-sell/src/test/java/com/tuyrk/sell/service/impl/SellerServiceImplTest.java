package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dataobject.SellerInfo;
import com.tuyrk.sell.service.SellerService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SellerServiceImplTest extends ApplicationTests {

    private static final String openid = "abc";

    @Autowired
    private SellerService sellerService;

    @Test
    public void findSellerInfoByOpenid() {
        SellerInfo result = sellerService.findSellerInfoByOpenid(openid);
        Assert.assertEquals(openid, result.getOpenid());
    }
}