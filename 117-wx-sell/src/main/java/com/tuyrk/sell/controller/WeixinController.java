package com.tuyrk.sell.controller;

import com.google.gson.Gson;
import com.tuyrk.sell.config.WeChatAccountConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/11 12:02 星期五
 * Description:
 * 微信测试号，微信公众号开发中token验证的解决办法，即接口配置信息中的url和token怎么设置的方法
 * https://blog.csdn.net/weixin_38306434/article/details/81384480
 */
@Slf4j
@RestController
@RequestMapping("/weixin")
public class WeixinController {

    @Autowired
    private WeChatAccountConfig weChatAccountConfig;

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入auth()方法...");
        log.info("code={}", code);

        RestTemplate restTemplate = new RestTemplate();

        String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + weChatAccountConfig.getMpAppId() + "&secret=" + weChatAccountConfig.getMpAppSecret() + "&code=" + code + "&grant_type=authorization_code";
        String response1 = restTemplate.getForObject(getOpenIdUrl, String.class);
        log.info("response1={}", response1);

        Response r = new Gson().fromJson(response1, Response.class);
        String getUserInfoURL = "https://api.weixin.qq.com/sns/userinfo?access_token=" + r.access_token + "&openid=" + r.openid + "&lang=zh_CN";
        String response2 = restTemplate.getForObject(getUserInfoURL, String.class);
        log.info("response2={}", response2);
    }
}

class Response {
    String access_token;
    Integer expires_in;
    String refresh_token;
    String openid;
    String scope;
}
/*
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe6a4566e548b4772&redirect_uri=http://tyk.nat300.top/sell/weixin/auth&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
code=011vRGu21cybDQ10FDr21FWnu21vRGu3
response1={
"access_token":"17_BaHTW4BPGKn8OLFfkCbd1wPuHjhielKhBWxMI8YS9h1XN-IjnK8HsS5G4OSRatgz_YRTROGhFyDNSEBoLnkU_VzrpDijlowwIk9-k_IFWBI",
"expires_in":7200,
"refresh_token":"17_N2OW-rUtwwes2PXEvGSvrWh3UAjqGN9lHvj9E8WKNiHHQi71AybNs1JEJf8f3wkO0tG9LBSfTGw_1kyKhrCFFWtzEt_OHlhQqP9JNO4-T60",
"openid":"oYnw56OEcTV8oekci1lk-ss-YvoQ",
"scope":"snsapi_userinfo"
}
response2={
"openid":"oYnw56OEcTV8oekci1lk-ss-YvoQ",
"nickname":"æ¶åå¤",
"sex":1,
"language":"zh_CN",
"city":"å¹¿å",
"province":"åå·",
"country":"ä¸­å½",
"headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/pVYBnckSuRJibLc6IOQiawqvpZyEkd5ZreR3spFhbiagQvywO9kOQpWFfHWr2QsSQgicnOKTdMAf6IzqnJ7tYr34nw\/132",
"privilege":[]
}
*/