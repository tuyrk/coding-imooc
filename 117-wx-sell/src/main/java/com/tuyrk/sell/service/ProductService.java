package com.tuyrk.sell.service;

import com.tuyrk.sell.dataobject.ProductInfo;
import com.tuyrk.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/9 19:26 星期三
 * Description:
 * 商品
 */
public interface ProductService {
    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品
     */
    List<ProductInfo> findUpAll();

    /**
     * 分页查询所有商品
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
