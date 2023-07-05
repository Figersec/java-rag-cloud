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
import com.kailin.util.SnowflakeIdUtil;
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
        log.info("{}在会话组:{},发送了一条消息:{}", webSocketServer.getUserId(),webSocketServer.getGroup().getChatId(), messageJson);
        try {
            WebSocketGroup chat = webSocketServer.getGroup();
            if (chat == null) {
                log.info("没有获取到会话组,chat：{}" , chat);
                return;
            }
            // 发送消息
            chat.sendInfoExcludeUser(messageJson.toJSONString(), webSocketServer.getUserId());

            ChatLogReq chatLog = ChatLogReq.builder()
                    .id(SnowflakeIdUtil.generateIdStr())
                    .content(messageJson.getString("content"))
                    .sendUserId(webSocketServer.getUserId())
                    .chatId(chat.getChatId())
                    .meta(messageJson.getString("meta"))
                    .recall(CommonEnum.NO.getValue()).build();
            iChatLogService.insertChatLog(chatLog);


            ChatReadRecordReq chatReadRecord = ChatReadRecordReq.builder()
                    .chatId(chat.getChatId())
                    .userId(webSocketServer.getUserId())
                    .chatLogId(chatLog.getId()).build();
            iChatReadRecordService.saveChatReadRecord(chatReadRecord);
        } catch (Exception e) {
            log.error("发送消息异常:{}", e);
        }
    }
}
