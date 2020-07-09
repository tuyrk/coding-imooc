package com.tuyrk.sell.form;

import com.tuyrk.sell.enums.ProductStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 14:52 星期三
 * Description:
 */
@Data
public class ProductForm {
    private String productId;
    /*名字*/
    private String productName;
    /*单价*/
    private BigDecimal productPrice;
    /*库存*/
    private Integer productStock;
    /*描述*/
    private String productDescription;
    /*小图*/
    private String productIcon;
    /*状态, 0正常1下架*/
    private Integer productStatus = ProductStatusEnum.UP.getCode();
    /*类目编号*/
    private Integer categoryType;
}
