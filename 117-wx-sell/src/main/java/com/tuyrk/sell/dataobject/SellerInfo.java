package com.tuyrk.sell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 18:03 星期三
 * Description:
 * 卖家信息
 */
@Data
@Entity
public class SellerInfo {
    // 卖家ID
    @Id
    private String sellerId;
    // 卖家用户名
    private String username;
    // 卖家密码
    private String password;
    // 卖家openid
    private String openid;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
}
