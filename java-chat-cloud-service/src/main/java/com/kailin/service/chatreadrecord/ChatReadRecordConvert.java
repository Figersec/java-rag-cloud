package com.kailin.service.chatreadrecord;

import com.kailin.dao.chat.entity.ChatReadRecord;
import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 转换类
 *
 * @author 杨松
 */
@Mapper(componentModel = "spring")
public interface ChatReadRecordConvert {


    ChatReadRecord req2do(ChatReadRecordReq chatReadRecordReq);

    List<ChatReadRecord> req2do(List<ChatReadRecordRes> chatUserReq);

    ChatReadRecordRes do2res(ChatReadRecord chatReadRecord);

    List<ChatReadRecordRes> do2res(List<ChatReadRecord> chatReadRecordList);
}
