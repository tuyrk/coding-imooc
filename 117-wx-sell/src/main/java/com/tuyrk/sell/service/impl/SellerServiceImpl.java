package com.tuyrk.sell.service.impl;

import com.tuyrk.sell.dataobject.SellerInfo;
import com.tuyrk.sell.repository.SellerRepository;
import com.tuyrk.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 18:18 星期三
 * Description:
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return sellerRepository.findByOpenid(openid);
    }
}
