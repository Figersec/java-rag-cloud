package com.kailin.service.websocket.factory;

import com.alibaba.fastjson.JSONObject;
import com.kailin.service.websocket.WebSocketServer;

/**
 * @author 杨松
 */
public abstract class AbstractRecoverTypeExecutor{

    /**
     * 执行接口
     * @param webSocketServer webSocket服务
     * @param messageJson 消息json
     */
    public abstract void execute(WebSocketServer webSocketServer, JSONObject messageJson);

}
