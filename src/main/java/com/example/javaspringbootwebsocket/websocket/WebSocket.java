package com.example.javaspringbootwebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.PathParam;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {
    private Session session;
    private String userId;
    private static ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, this);
        log.info("【websocket消息】有新的连接，总数为:" + webSocketMap.size());
        GroupSending("有新的连接，总数为:" + webSocketMap.size());
    }

}