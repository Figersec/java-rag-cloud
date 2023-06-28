package com.kailin.service.websocket.factory.handler;

import com.alibaba.fastjson.JSONObject;
import com.kailin.service.websocket.WebSocketGroup;
import com.kailin.service.websocket.WebSocketGroupManager;
import com.kailin.service.websocket.WebSocketServer;
import com.kailin.service.websocket.factory.AbstractRecoverTypeExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 心跳检测
 *
 * @author 杨松
 */
@Component("checkHeart")
@Slf4j
public class CheckHeart extends AbstractRecoverTypeExecutor {

    @Override
    public void execute(WebSocketServer webSocketServer, JSONObject messageJson) {
        try {
            String fromUserId = webSocketServer.getUserId();
            WebSocketGroup group = webSocketServer.getGroup();
            // 心跳检测直接返回
            WebSocketGroup targetGroup = WebSocketGroupManager.getGroup(group.getChatId());
            if (targetGroup != null) {
                targetGroup.sendInfoExcludeUser(messageJson.toJSONString(), fromUserId);
            } else {
                log.warn("请求的 chatId：{} 不存在", group.getChatId());
            }
        } catch (Exception e) {
            log.error("发送消息异常:{}", e);
        }
    }
}
