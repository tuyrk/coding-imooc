package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dataobject.ProductInfo;
import com.tuyrk.sell.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceImplTest extends ApplicationTests {
    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void findOne() {
        ProductInfo productInfo = productService.findOne("123456");
        Assert.assertNotNull(productInfo);
    }

    @Test
    public void findUp() {
        List<ProductInfo> productInfoUpList = productService.findUpAll();
        Assert.assertNotEquals(0, productInfoUpList.size());
    }

    @Test
    public void findAll() {
        PageRequest request = PageRequest.of(0, 2);
        Page<ProductInfo> productInfoUpList = productService.findAll(request);
        System.out.println(productInfoUpList.getTotalElements());
        Assert.assertNotEquals(0, productInfoUpList.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123457");
        productInfo.setProductName("皮皮虾");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很好吃的虾");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setCategoryType(2);
        ProductInfo result = productService.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void onSale() {
        ProductInfo productInfo = productService.onSale("123456");
        Assert.assertEquals(ProductStatusEnum.UP, productInfo.getProductStatusEnum());
    }

    @Test
    public void offSale() {
        ProductInfo productInfo = productService.offSale("123456");
        Assert.assertEquals(ProductStatusEnum.DOWN, productInfo.getProductStatusEnum());
    }
}