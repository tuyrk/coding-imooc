package com.tuyrk.sell.service;

import com.tuyrk.sell.dataobject.SellerInfo;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 18:16 星期三
 * Description:
 */
public interface SellerService {
    /**
     * 通过openid查询卖家的信息
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
