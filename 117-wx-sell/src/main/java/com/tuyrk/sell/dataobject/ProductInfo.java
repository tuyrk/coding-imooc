package com.tuyrk.sell.dataobject;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyrk.sell.enums.PayStatusEnum;
import com.tuyrk.sell.enums.ProductStatusEnum;
import com.tuyrk.sell.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/9 16:57 星期三
 * Description:
 * 商品
 */
@Data
@Entity
@DynamicUpdate
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 1617866465317633891L;
    @Id
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
    private Date createTime;
    private Date updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}
