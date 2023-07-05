package com.kailin.service.websocket.factory.handler;

import com.alibaba.fastjson.JSONObject;
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
            // 获取指定user的websocket对象
            if (webSocketServer != null) {
                log.info("检测心跳,userId{}",webSocketServer.getUserId());
                // 心跳检测直接返回
                webSocketServer.sendMessage(messageJson.toJSONString());
            }
        } catch (Exception e) {
            log.error("检测消息异常:{}", e);
        }
    }
}
