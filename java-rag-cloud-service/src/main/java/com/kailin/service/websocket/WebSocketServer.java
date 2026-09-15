package com.kailin.service.websocket;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kailin.enums.OperationTypeEnum;
import com.kailin.service.websocket.factory.MessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
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
     * WebSocketServer 所在的会话组
     */
    private WebSocketGroup group;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") String chatId, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        // 获取或者创建当前会话组
        group = WebSocketGroupManager.getOrCreateGroup(chatId);
        // 把当前 WebSocketServer 对象添加到所在会话组
        group.addWebSocketServer(userId, this);
        // 在线人数加一
        int count = ONLINE_COUNT.incrementAndGet();
        log.info("用户 {} 连接成功，会话组：{}，当前在线人数：{}", userId, group.getChatId(), count);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        // 把 WebSocketServer 对象从所在会话组移除
        group.removeWebSocketServer(userId);
        // 在线人数减一
        int count = ONLINE_COUNT.decrementAndGet();
        log.info("用户 {} 退出，会话组：{}，当前在线人数：{}", userId, group.getChatId(), count);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage(maxMessageSize = 10240000 )
    public void onMessage(String message) {
        log.info("收到消息：{}", message);
        if (StringUtils.isBlank(message)) {
            log.info("消息为空,中止转发");
            return;
        }
        JSONObject messageJson = new JSONObject();
        try {
            messageJson = JSON.parseObject(message);
        } catch (Exception e) {
            log.error("参数有误,转换Json对象失败: {}", e);
        }
        try {
            OperationTypeEnum operationTypeEnum = OperationTypeEnum.getEnumByValue(messageJson.getString("operationType"));
            MessageFactory messageFactory = SpringUtil.getBean(MessageFactory.class);
            messageFactory.getExecutor(operationTypeEnum).execute(group.getWebSocketServer(this.userId), messageJson);
        } catch (Exception e) {
            log.error("消息发送失败: {}", e);
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
