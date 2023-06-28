package com.kailin.service.chat;

import com.kailin.dao.chat.entity.Chat;
import com.kailin.request.chat.ChatReq;
import com.kailin.response.chat.ChatRes;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 转换类
 *
 * @author 杨松
 */
@Mapper(componentModel = "spring")
public interface ChatConvert {


    Chat req2do(ChatReq chatReq);

    List<Chat> req2do(List<ChatReq> chatReqList);

    ChatRes do2res(Chat chat);

    List<ChatRes> do2res(List<Chat> chatUser);

}
