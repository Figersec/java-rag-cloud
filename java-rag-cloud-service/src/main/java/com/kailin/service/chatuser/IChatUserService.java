package com.kailin.service.chatuser;

import com.kailin.request.chatuser.ChatUserReq;
import com.kailin.response.chatuser.ChatUserRes;

import java.util.List;

/**
 * @author 杨松
 */
public interface IChatUserService {

    /**
     * 新增聊天用户
     * @param chatUserList
     * @return
     */
    boolean saveChatUser(List<ChatUserReq> chatUserList);


    /**
     * 获取聊天用户集合
     * @param chatId
     * @return
     */
    List<ChatUserRes> getUserListByChatId(String chatId);
}
