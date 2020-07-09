package com.tuyrk.sell.repository;

import com.tuyrk.sell.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 18:07 星期三
 * Description:
 */
public interface SellerRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByOpenid(String openid);
}
