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
        try {
            WebSocketGroup group = webSocketServer.getGroup();
            String toUserId = messageJson.getString("toUserId");
            if (StringUtils.isNotBlank(toUserId)) {
                WebSocketServer target = group.getWebSocketServer(toUserId);
                if (target != null) {
                    // 发送消息
                    target.sendMessage(messageJson.toJSONString());
                } else {
                    log.warn("请求的 userId：{} 不在分组 {} 中", toUserId, group.getChatId());
                }
            }
            ChatLogReq build = ChatLogReq.builder()
                    .content(messageJson.getString("content"))
                    .sendUserId(loginUserUtil.getCurrentUserDetail().getUserId())
                    .chatId(messageJson.getString("chatId"))
                    .meta(messageJson.getString("meta"))
                    .recall(CommonEnum.NO.getValue()).build();
            iChatLogService.insertChatLog(build);
        } catch (Exception e) {
            log.error("发送消息异常:{}", e);
        }
    }
}
