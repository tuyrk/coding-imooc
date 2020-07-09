package com.tuyrk.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/17 20:39 星期四
 * Description:
 */
@Slf4j
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【WebSocket消息】有新的连接，总数：{}", webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【WebSocket消息】连接断开，总数：{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【WebSocket消息】收到客户端的消息：{}", message);
    }

    public void sendMessage(String message) {
        log.info("【WebSocket消息】广播消息，message={}", message);
        for (WebSocket websocket : webSocketSet) {
            try {
                websocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.info("【WebSocket消息】广播消息异常，message={}", message);
            }
        }
    }
}
