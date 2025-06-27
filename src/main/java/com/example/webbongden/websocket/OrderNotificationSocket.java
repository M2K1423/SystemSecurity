package com.example.webbongden.websocket;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/order")
public class OrderNotificationSocket {
    private static final Set<Session> adminSessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        adminSessions.add(session);
        System.out.println("Admin WebSocket Connected: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        adminSessions.remove(session);
        System.out.println("Admin WebSocket Disconnected: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket Error: " + error.getMessage());
    }

    public static void broadcast(String message) {
        for (Session session : adminSessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}
