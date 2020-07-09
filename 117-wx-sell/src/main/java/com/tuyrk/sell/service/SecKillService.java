package com.tuyrk.sell.service;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/19 22:05 星期六
 * Description:
 */
public interface SecKillService {

    String querySecKillProductInfo(String productId);

    void orderProductMockDiffUser(String productId);
}
