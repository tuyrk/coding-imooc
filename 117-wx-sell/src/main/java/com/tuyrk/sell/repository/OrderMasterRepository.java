package com.tuyrk.sell.repository;

import com.tuyrk.sell.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 8:28 星期四
 * Description:
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
