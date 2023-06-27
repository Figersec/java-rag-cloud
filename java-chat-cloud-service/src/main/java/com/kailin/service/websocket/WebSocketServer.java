package com.kailin.service.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ServerEndpoint("/socket/{groupId}/{userId}")
@Component
public class WebSocketServer {

    /**
     * 用来记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * WebSocketServer 对应的 Session
     */
    private Session session;

    /**
     * WebSocketServer 对应的用户 ID
     */
    private String userId;

    /**
     * WebSocketServer 所在的分组
     */
    private WebSocketGroup group;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("groupId") String groupId, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        // 获取或者创建当前分组
        group = WebSocketGroupManager.getOrCreateGroup(groupId);
        // 把当前 WebSocketServer 对象添加到所在分组
        group.addWebSocketServer(userId, this);
        // 在线人数加一
        int count = onlineCount.incrementAndGet();
        log.info("用户 {} 连接成功，分组：{}，当前在线人数：{}", userId, group.getGroupName(), count);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 把 WebSocketServer 对象从所在分组移除
        group.removeWebSocketServer(userId);
        // 在线人数减一
        int count = onlineCount.decrementAndGet();
        log.info("用户 {} 退出，分组：{}，当前在线人数：{}", userId, group.getGroupName(), count);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("用户消息：{}，报文：{}", userId, message);
        if (StringUtils.isNotBlank(message)) {
            try {
                JSONObject jsonObject = JSON.parseObject(message);
                String fromUserId = this.userId;
                String toGroupId = jsonObject.getString("toGroupId");
                String toUserId = jsonObject.getString("toUserId");
                if (StringUtils.isBlank(toGroupId) && StringUtils.isBlank(toUserId)) {
                    WebSocketGroup targetGroup = WebSocketGroupManager.getGroup(group.getGroupName());
                    if (targetGroup != null) {
                        targetGroup.sendInfoExcludeUser(jsonObject.toJSONString(), fromUserId);
                    } else {
                        log.warn("请求的 groupId：{} 不存在", group.getGroupName());
                    }
                } else if (StringUtils.isNotBlank(toUserId)) {
                    WebSocketServer target = group.getWebSocketServer(toUserId);
                    if (target != null) {
                        target.sendMessage(jsonObject.toJSONString());
                    } else {
                        log.warn("请求的 userId：{} 不在分组 {} 中", toUserId, group.getGroupName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 在发生异常时调用的回调函数
     */
    @OnError
    public void onError(Throwable error) {
        log.error("用户错误：{}，原因：{}", this.userId, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    // getter 方法
    public String getUserId() {
        return userId;
    }

    public WebSocketGroup getGroup() {
        return group;
    }
}
