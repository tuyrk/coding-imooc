package com.tuyrk.sell.repository;

import com.tuyrk.sell.ApplicationTests;
import com.tuyrk.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductCategoryRepositoryTest extends ApplicationTests {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest() {
        ProductCategory productCategory = repository.findById(1).orElse(null);
        System.out.println(productCategory);
    }

    @Test
    @Transactional
    public void saveTest() {
//        ProductCategory productCategory = repository.findById(2).orElse(null);
//        assert productCategory != null;
//        productCategory.setCategoryType(3);
//        repository.save(productCategory);

//        ProductCategory productCategory = new ProductCategory("女生最爱", 3);
        ProductCategory productCategory = new ProductCategory("男生最爱", 4);
        ProductCategory result = repository.save(productCategory);
        Assert.assertNotNull(result);
//        Assert.assertNotEquals(null, result);
    }

    @Test
    public void updateTest() {
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setCategoryId(2);
//        productCategory.setCategoryName("男生最爱");
//        productCategory.setCategoryType(3);
//        repository.save(productCategory);
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(2, 3, 4);
        List<ProductCategory> result = repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0, result.size());
    }
}