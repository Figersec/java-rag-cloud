package com.kailin.service.chatlog;

import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.response.chatlog.ChatLogRes;

import java.util.List;
import java.util.Map;

/**
 * @author 杨松
 */
public interface IChatLogService {



    /**
     * 插入聊天日志
     * @param chatLogReq
     * @return
     */
    boolean insertChatLog(ChatLogReq chatLogReq);


    /**
     * 根据chatId(房间id)获取聊天记录
     * @param chatId
     * @return
     */
    Map<String, Object> getChatLogByChatId(String chatId);


    /**
     * 更新撤回状态
     * @param chatLogId
     * @return
     */
    boolean updateRecallStatus(String chatLogId, Integer status);








}
