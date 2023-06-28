package com.kailin.service.chat;

import com.kailin.request.chat.ChatReq;
import com.kailin.response.chat.ChatRes;

/**
 * @author 杨松
 */
public interface IChatService {


    /**
     * 打开房间
     * @param chatReq
     * @return
     */
    ChatRes openChat(ChatReq chatReq);

    /**
     * 创建房间
     *
     * @param chatReq
     * @return
     */
    ChatRes createChat(ChatReq chatReq);


    /**
     * 查询房间信息
     * @param chatId
     * @return
     */
    ChatRes getChatById(String chatId);


}
