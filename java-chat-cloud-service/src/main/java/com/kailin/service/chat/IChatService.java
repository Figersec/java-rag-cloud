package com.kailin.service.chat;

import com.kailin.dao.chat.entity.Chat;
import com.kailin.dao.chat.entity.ChatLog;

/**
 * @author 杨松
 */
public interface IChatService {


    /**
     * 创建聊天
     * @param chatLog
     * @return
     */
    Chat createChat(ChatLog chatLog);


}
