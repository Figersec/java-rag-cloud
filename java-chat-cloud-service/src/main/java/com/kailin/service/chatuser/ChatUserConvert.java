package com.kailin.service.chatuser;

import com.kailin.dao.chat.entity.ChatUser;
import com.kailin.request.chatuser.ChatUserReq;
import com.kailin.response.chatuser.ChatUserRes;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 转换类
 * @author 杨松
 */
@Mapper(componentModel = "spring")
public interface ChatUserConvert {


    ChatUser req2do(ChatUserReq chatUserReq);

    List<ChatUser> req2do(List<ChatUserReq> chatUserReq);

    ChatUserRes do2res(ChatUser chatUser);

    List<ChatUserRes> do2res(List<ChatUser> chatUser);
}
