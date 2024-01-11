package com.example.javaspringbootwebsocket.websocket;

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
    private static final ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, this);
        log.info("【websocket消息】有新的连接，总数为:" + webSocketMap.size());
        /*
        * GroupSending is a method that sends a message to all users.
        * */
        GroupSending("有新的连接，总数为:" + webSocketMap.size());
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this.userId);
        log.info("【websocket消息】连接断开，总数为:" + webSocketMap.size());
        GroupSending("连接断开，总数为:" + webSocketMap.size());
    }

    @OnMessage
    public void onMessage(String messageStr) {
        log.info("【websocket消息】收到客户端发来的消息:" + messageStr);

        if (messageStr.indexOf("TOUSER") == 0) {
            String[] split = messageStr.split(";");
            String[] split1 = split[0].split(":");
            String[] split2 = split[1].split(":");

            String userId = split1[1];
            String message = split2[1];

            AppointSending(userId, message);
        } else {
            // 群发
            GroupSending(messageStr);
        }
    }

    /*
    *  群发消息
    * */
    public void GroupSending(String message) {
        for (String key : webSocketMap.keySet()) {
            try {
                webSocketMap.get(key).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("【websocket消息】群发消息失败:" + e.getMessage());
            }
        }
    }

    /*
    * 指定发送消息
    *
    * */
    public void AppointSending(String userId, String message) {
        try {
            webSocketMap.get(userId).session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 发生错误时调用
    * */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("【websocket消息】发生错误:" + error.getMessage());
        error.printStackTrace();
    }

}