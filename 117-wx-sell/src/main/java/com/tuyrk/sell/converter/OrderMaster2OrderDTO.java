package com.tuyrk.sell.converter;

import com.tuyrk.sell.dataobject.OrderMaster;
import com.tuyrk.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 12:13 星期四
 * Description:
 */
public class OrderMaster2OrderDTO {
    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(OrderMaster2OrderDTO::convert).collect(Collectors.toList());
    }
}
