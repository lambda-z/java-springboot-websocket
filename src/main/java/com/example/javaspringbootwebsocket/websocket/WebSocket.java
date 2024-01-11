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

}