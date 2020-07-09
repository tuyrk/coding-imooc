package com.tuyrk.sell.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/12 14:29 星期六
 * Description:
 * 配置微信支付商户相关信息
 */
@Component
public class WeChatPayConfig {
    @Autowired
    private WeChatAccountConfig accountConfig;

    @Bean
    public BestPayServiceImpl bestPayService() {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config());
        return bestPayService;
    }

    public WxPayH5Config wxPayH5Config() {
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setAppId(accountConfig.getPayMpAppId());
        wxPayH5Config.setMchId(accountConfig.getPayMchId());
        wxPayH5Config.setMchKey(accountConfig.getPayMchKey());
        wxPayH5Config.setKeyPath(accountConfig.getPayKeyPath());
        wxPayH5Config.setNotifyUrl(accountConfig.getPayNotifyUrl());
        return wxPayH5Config;
    }
}
