package com.tuyrk.sell.dataobject;

import com.tuyrk.sell.enums.OrderStatusEnum;
import com.tuyrk.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 8:10 星期四
 * Description:
 * 订单主表
 */
@Data
@Entity
@DynamicUpdate
public class OrderMaster {
    /*订单ID*/
    @Id
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
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    /*支付状态，默认0未支付*/
    private Integer payStatus = PayStatusEnum.WAIT.getCode();
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;

//    @Transient
//    private List<OrderDetail> orderDetailList;
}
