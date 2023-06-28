package com.kailin.service.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 分组管理器
 *
 * @author 杨松
 */
@Component
public class WebSocketGroupManager {
    /**
     * 用来存放所有分组
     */
    private static final Map<String, WebSocketGroup> webSocketGroupMap = new ConcurrentHashMap<>();

    /**
     * 获取或者创建指定分组
     */
    public static WebSocketGroup getOrCreateGroup(String chatId) {
        return webSocketGroupMap.computeIfAbsent(chatId, k -> new WebSocketGroup(chatId));
    }

    public static WebSocketGroup getGroup(String groupName) {
        return webSocketGroupMap.get(groupName);
    }

    /**
     * 添加新分组
     */
    public static void addGroup(WebSocketGroup group) {
        // 把指定分组添加到映射表中
        webSocketGroupMap.put(group.getChatId(), group);
    }

    /**
     * 获取指定用户的 WebSocketServer 对象
     */
    public static WebSocketServer getWebSocketServer(String groupId, String userId) {
        // 获取指定分组
        WebSocketGroup group = webSocketGroupMap.get(groupId);
        // 如果该分组存在
        if (group != null) {
            // 返回该分组中指定用户的 WebSocketServer 对象
            return group.getWebSocketMap().get(userId);
        }
        return null;
    }
}
