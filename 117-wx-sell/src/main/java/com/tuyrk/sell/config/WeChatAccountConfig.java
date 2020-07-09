package com.tuyrk.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/11 14:16 星期五
 * Description:
 * 微信公众号账户信息
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatAccountConfig {
    //公众平台id
    private String mpAppId;
    //公众平台密钥
    private String mpAppSecret;
    //开放平台id
    private String openAppId;
    //开放平台密钥
    private String openAppSecret;
    //支付公众号appId
    private String payMpAppId;
    //支付商户号
    private String payMchId;
    //支付商户密钥
    private String payMchKey;
    //支付商户证书路径
    private String payKeyPath;
    //支付微信支付异步通知地址
    private String payNotifyUrl;
    //微信模板Id
    private Map<String,String> templateId;
}