package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.dataobject.ProductCategory;
import com.tuyrk.sell.dataobject.dao.ProductCategoryDao;
import com.tuyrk.sell.dataobject.mapper.ProductCategoryMapper;
import com.tuyrk.sell.repository.ProductCategoryRepository;
import com.tuyrk.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/9 16:43 星期三
 * Description:
 * 类目
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ProductCategoryRepository repository;

//    @Autowired
//    private ProductCategoryMapper mapper;
//
//    @Autowired
//    private ProductCategoryDao dao;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findById(categoryId).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
