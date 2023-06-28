package com.kailin.service.websocket;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 分组
 *
 * @author 杨松
 */
@Component
public class WebSocketGroup {
    private final String chatId;

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    private Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    public WebSocketGroup() {
        this("default");
    }

    public WebSocketGroup(String chatId) {
        this.chatId = chatId;
    }

    public void addWebSocketServer(String userId, WebSocketServer webSocketServer) {
        // 使用 computeIfAbsent() 方法添加 WebSocketServer 对象
        webSocketMap.computeIfAbsent(userId, k -> webSocketServer);
    }

    public void removeWebSocketServer(String userId) {
        webSocketMap.remove(userId);
    }

    /**
     * 向分组中的所有客户端发送消息
     *
     * @param message 待发送的消息
     */
    public void sendInfo(String message) {
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            webSocketServer.sendMessage(message);
        }
    }

    /**
     * 发送消息给分组内所有用户，排除指定用户
     *
     * @param message       消息内容
     * @param excludeUserId 要排除的用户 ID
     */
    public void sendInfoExcludeUser(String message, String excludeUserId) {
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            if (!webSocketServer.getUserId().equals(excludeUserId)) {
                webSocketServer.sendMessage(message);
            }
        }
    }

    public WebSocketServer getWebSocketServer(String userId) {
        return webSocketMap.get(userId);
    }

    public int getOnlineCount() {
        return webSocketMap.size();
    }

    // getter 方法
    public String getChatId() {
        return chatId;
    }

    public Map<String, WebSocketServer> getWebSocketMap() {
        return webSocketMap;
    }
}
