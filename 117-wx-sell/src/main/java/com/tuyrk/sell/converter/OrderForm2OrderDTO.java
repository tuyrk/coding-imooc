package com.tuyrk.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuyrk.sell.dataobject.OrderDetail;
import com.tuyrk.sell.dto.OrderDTO;
import com.tuyrk.sell.enums.ResultEnum;
import com.tuyrk.sell.exception.SellException;
import com.tuyrk.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 16:23 星期四
 * Description:
 */
@Slf4j
public class OrderForm2OrderDTO {
    public static OrderDTO convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList;
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception e) {
            log.error("【对象转换】错误", orderForm.getItems());
            throw new SellException((ResultEnum.PARAM_ERROR));
        }

        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
