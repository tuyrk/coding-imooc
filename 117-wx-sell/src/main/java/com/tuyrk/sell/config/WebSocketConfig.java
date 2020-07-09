package com.tuyrk.sell.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/17 20:38 星期四
 * Description:
 */
@Component
public class WebSocketConfig {
    @Bean
    private ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
