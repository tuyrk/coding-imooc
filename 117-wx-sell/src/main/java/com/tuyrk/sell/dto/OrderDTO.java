package com.tuyrk.sell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tuyrk.sell.dataobject.OrderDetail;
import com.tuyrk.sell.enums.OrderStatusEnum;
import com.tuyrk.sell.enums.PayStatusEnum;
import com.tuyrk.sell.utils.EnumUtil;
import com.tuyrk.sell.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 9:11 星期四
 * Description:
 */
@Data
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    /*订单ID*/
    private String orderId;
    /*买家名字*/
    private String buyerName;
    /*买家手机号*/
    private String buyerPhone;
    /*买家地址*/
    private String buyerAddress;
    /*买家微信openid*/
    private String buyerOpenid;
    /*订单金额*/
    private BigDecimal orderAmount;
    /*订单状态，默认0新下单*/
    private Integer orderStatus;
    /*支付状态，默认0未支付*/
    private Integer payStatus;
    /*创建时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    /*更新时间*/
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }
}
