package com.kailin.service.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 会话组管理器
 *
 * @author 杨松
 */
@Component
public class WebSocketGroupManager {
    /**
     * 用来存放所有会话组
     */
    private static final Map<String, WebSocketGroup> webSocketGroupMap = new ConcurrentHashMap<>();

    /**
     * 获取或者创建指定会话组
     */
    public static WebSocketGroup getOrCreateGroup(String chatId) {
        return webSocketGroupMap.computeIfAbsent(chatId, k -> new WebSocketGroup(chatId));
    }

    public static WebSocketGroup getGroup(String groupId) {
        return webSocketGroupMap.get(groupId);
    }

    /**
     * 添加新会话组
     */
    public static void addGroup(WebSocketGroup group) {
        // 把指定会话组添加到映射表中
        webSocketGroupMap.put(group.getChatId(), group);
    }

    /**
     * 获取指定用户的 WebSocketServer 对象
     */
    public static WebSocketServer getWebSocketServer(String groupId, String userId) {
        // 获取指定会话组
        WebSocketGroup group = webSocketGroupMap.get(groupId);
        // 如果该会话组存在
        if (group != null) {
            // 返回该会话组中指定用户的 WebSocketServer 对象
            return group.getWebSocketMap().get(userId);
        }
        return null;
    }
}
