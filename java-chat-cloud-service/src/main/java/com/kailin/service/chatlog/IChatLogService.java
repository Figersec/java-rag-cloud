package com.kailin.service.chatlog;

import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.request.chatlog.ChatLogReq;

import java.util.List;

/**
 * @author 杨松
 */
public interface IChatLogService {

    /**
     * 条件获取聊天记录
     * @param chatLog
     * @return
     */
    List<ChatLog> getChatContentByCondition(ChatLog chatLog);

    /**
     * 插入聊天日志
     * @param chatLogReq
     * @return
     */
    boolean insertChatLog(ChatLogReq chatLogReq);







}
