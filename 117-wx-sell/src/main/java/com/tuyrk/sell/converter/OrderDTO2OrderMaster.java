package com.tuyrk.sell.converter;

import com.tuyrk.sell.dataobject.OrderMaster;
import com.tuyrk.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 14:41 星期四
 * Description:
 */
public class OrderDTO2OrderMaster {
    public static OrderMaster convert(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        return orderMaster;
    }
}
