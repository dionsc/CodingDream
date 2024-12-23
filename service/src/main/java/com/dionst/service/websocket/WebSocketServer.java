package com.dionst.service.websocket;

import com.rabbitmq.client.LongString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import javax.websocket.server.PathParam;

@Component
@ServerEndpoint("/ws/{contestId}")
@Slf4j
public class WebSocketServer {

    // 存放会话对象
    private static final Map<String, Set<Session>> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("contestId") String contestId) {
        sessionMap.computeIfAbsent(contestId, k -> new HashSet<>()).add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("contestId") String contestId) {
        Set<Session> sessions = sessionMap.get(contestId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionMap.remove(contestId);
            }
        }
    }

    // 向参加比赛用户广播消息
    public void sendToAllClient(Long contestId, String message) throws IOException {
        Set<Session> sessions = sessionMap.get(contestId.toString());
        if (sessions != null) {
            for (Session session : sessions) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
}