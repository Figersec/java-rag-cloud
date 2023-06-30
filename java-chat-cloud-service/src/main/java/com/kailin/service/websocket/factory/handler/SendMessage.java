package com.kailin.service.websocket.factory.handler;

import com.alibaba.fastjson.JSONObject;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.enums.CommonEnum;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.service.chatlog.IChatLogService;
import com.kailin.service.utils.LoginUserUtil;
import com.kailin.service.websocket.WebSocketGroup;
import com.kailin.service.websocket.WebSocketServer;
import com.kailin.service.websocket.factory.AbstractRecoverTypeExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    private LoginUserUtil loginUserUtil;

    @Override
    public void execute(WebSocketServer webSocketServer, JSONObject messageJson) {
        log.info("{}发送了一条消息:{}",webSocketServer.getUserId(),messageJson);
        try {
            WebSocketGroup chat = webSocketServer.getGroup();
            if (chat != null) {
                // 发送消息
                chat.sendInfoExcludeUser(messageJson.toJSONString(),webSocketServer.getUserId());
            }
            ChatLogReq build = ChatLogReq.builder()
                    .content(messageJson.getString("content"))
                    .sendUserId(webSocketServer.getUserId())
                    .chatId(messageJson.getString("chatId"))
                    .meta(messageJson.getString("meta"))
                    .recall(CommonEnum.NO.getValue()).build();
            iChatLogService.insertChatLog(build);
        } catch (Exception e) {
            log.error("发送消息异常:{}", e);
        }
    }
}
