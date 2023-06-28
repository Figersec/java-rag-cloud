package com.kailin.service.websocket.factory.handler;

import com.alibaba.fastjson.JSONObject;
import com.kailin.service.websocket.WebSocketGroup;
import com.kailin.service.websocket.WebSocketServer;
import com.kailin.service.websocket.factory.AbstractRecoverTypeExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 撤回消息实现
 *
 * @author 杨松
 */
@Component("recall")
@Slf4j
public class RecallMessage extends AbstractRecoverTypeExecutor {

    @Override
    public void execute(WebSocketServer webSocketServer, JSONObject messageJson) {
        try {
            WebSocketGroup group = webSocketServer.getGroup();
            if (null != group) {
                group.sendInfoExcludeUser(messageJson.toJSONString(), null);
            }
        } catch (Exception e) {
            log.error("撤回消息异常:{}", e);
        }
    }
}
