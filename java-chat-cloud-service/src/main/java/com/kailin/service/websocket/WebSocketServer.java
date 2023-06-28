package com.kailin.service.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kailin.enums.OperationTypeEnum;
import com.kailin.service.websocket.factory.MessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 杨松
 */
@Slf4j
@ServerEndpoint("/socket/{chatId}/{userId}")
@Component
public class WebSocketServer {

    /**
     * 用来记录当前在线连接数
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

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

    @Autowired
    private MessageFactory messageFactory;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") String chatId, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        // 获取或者创建当前分组
        group = WebSocketGroupManager.getOrCreateGroup(chatId);
        // 把当前 WebSocketServer 对象添加到所在分组
        group.addWebSocketServer(userId, this);
        // 在线人数加一
        int count = ONLINE_COUNT.incrementAndGet();
        log.info("用户 {} 连接成功，分组：{}，当前在线人数：{}", userId, group.getChatId(), count);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 把 WebSocketServer 对象从所在分组移除
        group.removeWebSocketServer(userId);
        // 在线人数减一
        int count = ONLINE_COUNT.decrementAndGet();
        log.info("用户 {} 退出，分组：{}，当前在线人数：{}", userId, group.getChatId(), count);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到消息：{}", message);
        if (StringUtils.isBlank(message)) {
            log.info("消息为空,中止转发");
            return;
        }
        try {
            JSONObject messageJson = JSON.parseObject(message);
            OperationTypeEnum operationTypeEnum = OperationTypeEnum.getEnumByValue(messageJson.getString("operationType"));
            messageFactory.getExecutor(operationTypeEnum).execute(group.getWebSocketServer(this.userId), messageJson);
        } catch (Exception e) {
            log.error("json消息格式转换失败: {}", e);
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
