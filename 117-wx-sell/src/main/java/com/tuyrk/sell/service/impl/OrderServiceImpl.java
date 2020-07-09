package com.tuyrk.sell.service.impl;

import com.lly835.bestpay.model.PayResponse;
import com.tuyrk.sell.converter.OrderMaster2OrderDTO;
import com.tuyrk.sell.dataobject.OrderDetail;
import com.tuyrk.sell.dataobject.OrderMaster;
import com.tuyrk.sell.dataobject.ProductInfo;
import com.tuyrk.sell.dto.CartDTO;
import com.tuyrk.sell.dto.OrderDTO;
import com.tuyrk.sell.enums.OrderStatusEnum;
import com.tuyrk.sell.enums.PayStatusEnum;
import com.tuyrk.sell.enums.ResultEnum;
import com.tuyrk.sell.exception.SellException;
import com.tuyrk.sell.repository.OrderDetailRepository;
import com.tuyrk.sell.repository.OrderMasterRepository;
import com.tuyrk.sell.service.*;
import com.tuyrk.sell.utils.KeyUtil;
import com.tuyrk.sell.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 9:15 星期四
 * Description:
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private PayService payService;
    @Autowired
    private PushMessageService pushMessageService;
    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        String orderId = KeyUtil.getUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
//        List<CartDTO> cartDTOList = new ArrayList<>();
        //1. 查询商品（数量，价格）

        // TODO: 2019-1-10 10:58:42 循环查询这里可不可以改一改？
        // 初步想法：
        //1.获取List<ProductInfo>,构建Map<ProductInfo.getProductId(), ProductInfo>
        //2.ProductInfo productInfo = map.get(orderDetail.getProductId());
        ////这里有个问题，通过通过list.getProductId()获取productInfo么

        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {//商品不存在
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2. 计算订单总价
            orderAmount = orderAmount.add(productInfo.getProductPrice().
                    multiply(new BigDecimal(orderDetail.getProductQuantity()))
            );
            //3. 写入订单数据库（order_master和order_detail）
            //3.1写入订单数据库-订单详情入库(order_detail)
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);

//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }
        //3.2写入订单数据库-订单入库(order_master)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        //4. 扣库存（别忘了）//判断数量是否充足
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        // 发送WebSocket消息
        webSocket.sendMessage(orderDTO.getOrderId());
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (orderDetailList.isEmpty()) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        if (StringUtils.isEmpty(buyerOpenid)) {
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convert(orderMasterPage.getContent());
        // 这里为什么不返回List<OrderDTO>？
        // 因为前端有时候不只是需要每页的内容，还需要当前页码，总页数等一系列信息
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(String orderId) {
        //判断订单状态
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            log.error("【取消订单】订单不存在，orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确，orderId={}，orderStatus={}", orderId, orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (null == updateResult) {
            log.error("【取消订单】更新失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //反还库存
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            log.error("【取消订单】订单中无商品详情，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDetailList.stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
        //如果已支付，需要退款
        OrderDTO orderDTO = OrderMaster2OrderDTO.convert(orderMaster);
        if (orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderDTO);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        //判断订单是否存在
        if (orderMaster == null) {
            log.error("【完成订单】订单不存在，orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断订单状态
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完成订单】订单状态不正确，orderId={}，orderStatus={}", orderId, orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if (!orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            log.error("【完成订单】支付状态不正确，orderId={}，orderStatus={}", orderId, orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (null == updateResult) {
            log.error("【完成订单】更新失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //推送微信模板消息
        pushMessageService.orderStatus(OrderMaster2OrderDTO.convert(orderMaster));
        return OrderMaster2OrderDTO.convert(orderMaster);
    }

    @Override
    @Transactional
    public OrderDTO paid(PayResponse payResponse) {
        OrderMaster orderMaster = orderMasterRepository.findById(payResponse.getOrderId()).orElse(null);
        //判断订单是否存在
        if (orderMaster == null) {
            log.error("【支付订单】订单不存在，orderId={}", payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断订单状态
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【支付订单】订单状态不正确，orderId={}，orderStatus={}", payResponse.getOrderId(), orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断金额是否一致(0.1 0.10)
        if (!MathUtil.equals(orderMaster.getOrderAmount().doubleValue(), payResponse.getOrderAmount())) {
            log.error("【微信支付】异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderMaster.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }
        //判断支付状态
        if (!orderMaster.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【支付订单】支付状态不正确，orderDTO={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderMaster.setOrderStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【支付订单】更新失败，orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return OrderMaster2OrderDTO.convert(orderMaster);
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTO.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
