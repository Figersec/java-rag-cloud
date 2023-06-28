package com.kailin.service.chatlog;

import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.response.chatlog.ChatLogRes;

import java.util.List;

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
     * 条件获取聊天记录
     * @param chatId
     * @return
     */
    List<ChatLogRes> getChatContentByCondition(String chatId);







}
