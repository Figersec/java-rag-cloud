package com.kailin.service.websocket.factory.handler;

import com.alibaba.fastjson.JSONObject;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.enums.CommonEnum;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.service.chatlog.IChatLogService;
import com.kailin.service.chatreadrecord.IChatReadRecordService;
import com.kailin.service.websocket.WebSocketGroup;
import com.kailin.service.websocket.WebSocketServer;
import com.kailin.service.websocket.factory.AbstractRecoverTypeExecutor;
import com.kailinjt.middleware.kp.common.util.redisson.lock.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发消息实现
 *
 * @author 杨松
 */
@Component("send")
@Slf4j
public class SendMessage extends AbstractRecoverTypeExecutor {

    @Autowired
    private IChatLogService iChatLogService;
    @Autowired
    private IChatReadRecordService iChatReadRecordService;

    @Override
    @RedisLock(key = "#webSocketServer.userId")
    public void execute(WebSocketServer webSocketServer, JSONObject messageJson) {
        log.info("{}发送了一条消息:{}", webSocketServer.getUserId(), messageJson);
        try {
            WebSocketGroup chat = webSocketServer.getGroup();
            if (chat != null) {
                // 发送消息
                chat.sendInfoExcludeUser(messageJson.toJSONString(), webSocketServer.getUserId());
            }
            String chatId = messageJson.getString("chatId");
            ChatLogReq chatLog = ChatLogReq.builder()
                    .content(messageJson.getString("content"))
                    .sendUserId(webSocketServer.getUserId())
                    .chatId(chatId)
                    .meta(messageJson.getString("meta"))
                    .recall(CommonEnum.NO.getValue()).build();
            ChatLog chatLogResul = iChatLogService.insertChatLog(chatLog);


            ChatReadRecordReq chatReadRecord = ChatReadRecordReq.builder()
                    .chatId(chatId)
                    .userId(webSocketServer.getUserId())
                    .chatLogId(chatLogResul.getId()).build();
            iChatReadRecordService.saveChatReadRecord(chatReadRecord);
        } catch (Exception e) {
            log.error("发送消息异常:{}", e);
        }
    }
}
