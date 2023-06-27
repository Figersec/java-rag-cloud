package com.kailin.service;

import com.kailin.dao.chat.entity.ChatLog;

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
     * @param chatLog
     * @return
     */
    boolean insertChatLog(ChatLog chatLog);


    /**
     * 更新消息已读状态
     * @param chatLog
     * @return
     */
    boolean updateReadStatus(ChatLog chatLog);


    /**
     * 清空消息
     * @param chatLog
     * @return
     */
    boolean clearChat(ChatLog chatLog);




}
