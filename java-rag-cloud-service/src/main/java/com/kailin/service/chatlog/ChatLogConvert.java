package com.kailin.service.chatlog;

import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.response.chatlog.ChatLogRes;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 转换类
 *
 * @author 杨松
 */
@Mapper(componentModel = "spring")
public interface ChatLogConvert {

    ChatLog req2do(ChatLogReq chatLogReq);

    List<ChatLog> req2do(List<ChatLogReq> chatUserReq);

    ChatLogRes do2res(ChatLog chatLog);

    List<ChatLogRes> do2res(List<ChatLog> chatUser);
}
