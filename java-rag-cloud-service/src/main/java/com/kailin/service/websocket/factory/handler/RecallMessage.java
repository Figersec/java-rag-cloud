package com.kailin.service.websocket.factory.handler;

import com.alibaba.fastjson.JSONObject;
import com.kailin.enums.CommonEnum;
import com.kailin.service.chatlog.IChatLogService;
import com.kailin.service.websocket.WebSocketGroup;
import com.kailin.service.websocket.WebSocketServer;
import com.kailin.service.websocket.factory.AbstractRecoverTypeExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 撤回消息实现
 *
 * @author 杨松
 */
@Component("recall")
@Slf4j
@RequiredArgsConstructor
public class RecallMessage extends AbstractRecoverTypeExecutor {

    private final IChatLogService iChatLogService;

    @Override
    public void execute(WebSocketServer webSocketServer, JSONObject messageJson) {
        log.info("{}撤回了一条消息:{}",webSocketServer.getUserId(),messageJson);
        try {
            WebSocketGroup group = webSocketServer.getGroup();
            if (null != group) {
                // 更新消息为撤回状态后给聊天组所有人发送撤回通知
                boolean success = iChatLogService.updateRecallStatus(messageJson.getString("chatLogId"), CommonEnum.YES.getValue());
                if (success) {
                    group.sendInfoExcludeUser(messageJson.toJSONString(), null);
                }

            }

        } catch (Exception e) {
            log.error("撤回消息异常:{}", e);
        }
    }
}
