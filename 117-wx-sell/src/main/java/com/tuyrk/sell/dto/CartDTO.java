package com.tuyrk.sell.dto;

import lombok.Data;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 10:31 星期四
 * Description:
 * 购物车
 */
@Data
public class CartDTO {
    /*商品ID*/
    private String productId;
    /*商品数量*/
    private Integer productQuantity;

    // TODO: 2019/1/10 此处没有无参构造方法
//    public CartDTO() {
//    }

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
