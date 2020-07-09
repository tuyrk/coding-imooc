package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.exception.SellException;
import com.tuyrk.sell.service.RedisLock;
import com.tuyrk.sell.service.SecKillService;
import com.tuyrk.sell.utils.KeyUtil;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/19 22:06 星期六
 * Description:
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    private final static long TIMEOUT = 10 * 1000;//超时时间 10s

    @Autowired
    private RedisLock redisLock;

    /**
     * 国庆活动，皮蛋粥特价，限量10000份
     */
    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;

    static {
        /**
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000);
        stock.put("123456", 100000);
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return "国庆活动，皮蛋粥特价，限量份"
                + products.get(productId)
                + "还剩：" + stock.get(productId) + "份"
                + "该商品成功下单用户数目："
                + orders.size() + "人";
    }

    @Override
    public void orderProductMockDiffUser(String productId) {

        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if (!redisLock.lock(productId, String.valueOf(time))) {
            throw new SellException(101, "哎呦喂，人也太多了，换个姿势再试试~~");
        }
        //1. 查询该订单库存，为0则活动结束。
        int stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new SellException(100, "活动结束");
        }
        //2. 下单（模拟不同用户openid不同）
        orders.put(KeyUtil.getUniqueKey(), productId);
        //3. 减库存
        stockNum -= 1;
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stock.put(productId, stockNum);

        //解锁
        redisLock.unlock(productId, String.valueOf(time));
    }
}
