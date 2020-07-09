package com.tuyrk.sell.controller;

import com.lly835.bestpay.model.PayResponse;
import com.tuyrk.sell.dto.OrderDTO;
import com.tuyrk.sell.enums.ResultEnum;
import com.tuyrk.sell.exception.SellException;
import com.tuyrk.sell.service.OrderService;
import com.tuyrk.sell.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/12 14:10 星期六
 * Description:
 */
@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;

    @GetMapping("/pay")//注意context-path
    public ModelAndView index(@RequestParam("openid")String openid,
                      @RequestParam("orderId") String orderId,
                      @RequestParam("returnUrl") String returnUrl,
                      Map<String, Object> map){
        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.error("【微信支付】订单不存在，orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2.发起支付
//        orderDTO.setBuyerOpenid(openid);
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }

    /**
     * 创建订单
     */
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {
        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.error("【微信支付】订单不存在，orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2.发起支付
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }

    /**
     * 微信异步通知
     */
    @PostMapping("/notify")
    public ModelAndView notify2(@RequestBody String notifyData) {
        payService.notify(notifyData);
        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }


}
