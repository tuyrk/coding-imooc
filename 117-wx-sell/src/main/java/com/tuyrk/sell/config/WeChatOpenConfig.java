package com.tuyrk.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/16 20:19 星期三
 * Description:
 * 微信开放平台
 */
@Component
public class WeChatOpenConfig {
    @Autowired
    private WeChatAccountConfig accountConfig;

    @Bean
    public WxMpService wxOpenService() {
        WxMpService wxOpenService = new WxMpServiceImpl();
        wxOpenService.setWxMpConfigStorage(wxOpenConfigStorage());
        return wxOpenService;
    }

    @Bean
    public WxMpConfigStorage wxOpenConfigStorage() {
        WxMpInMemoryConfigStorage wxOpenConfigStorage = new WxMpInMemoryConfigStorage();
        wxOpenConfigStorage.setAppId(accountConfig.getOpenAppId());
        wxOpenConfigStorage.setSecret(accountConfig.getOpenAppSecret());
        return wxOpenConfigStorage;
    }
}
