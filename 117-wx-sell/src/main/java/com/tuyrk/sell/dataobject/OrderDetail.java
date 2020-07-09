package com.tuyrk.sell.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tuyrk.sell.utils.serializer.Date2LongSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 8:21 星期四
 * Description:
 */
@Data
@Entity
@DynamicUpdate
public class OrderDetail {
    @Id
    private String detailId;
    /*订单ID*/
    private String orderId;
    /*商品ID*/
    private String productId;
    /*商品名称*/
    private String productName;
    /*商品单价*/
    private BigDecimal productPrice;
    /*商品数量*/
    private Integer productQuantity;
    /*商品小图*/
    private String productIcon;
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;
}
