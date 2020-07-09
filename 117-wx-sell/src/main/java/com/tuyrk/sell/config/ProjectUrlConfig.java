package com.tuyrk.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 20:43 星期三
 * Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "project-url")
public class ProjectUrlConfig {
    // 微信公众平台授权URL
    private String wechatMpAuthorize;
    // 微信开放平台授权URL
    private String wechatOpenAuthorize;
    // 点餐系统
    private String sell;
}
