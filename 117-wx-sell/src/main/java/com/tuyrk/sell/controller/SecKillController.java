package com.tuyrk.sell.controller;

import com.tuyrk.sell.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/19 21:58 星期六
 * Description:
 */
@RestController
@RequestMapping("/skill")
@Slf4j
public class SecKillController {
    @Autowired
    private SecKillService secKillService;

    /**
     * 查询秒杀活动特价商品的信息
     */
    @GetMapping("/query/{productId}")
    public String query(@PathVariable String productId) {
        return secKillService.querySecKillProductInfo(productId);
    }

    /**
     * 秒杀，没有抢到获得"哎呦喂，xxxxx"，抢到了会返还剩余的库存量
     */
    @GetMapping("/order/{productId}")
    public String skill(@PathVariable String productId) {
        log.info("@skill request，productId：={}", productId);
        secKillService.orderProductMockDiffUser(productId);
        return secKillService.querySecKillProductInfo(productId);
    }
}
