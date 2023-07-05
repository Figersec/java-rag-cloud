package com.kailin.service.chatreadrecord;

import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.request.chatreadrecord.UpdateChatRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;

import java.util.List;

/**
 * @author 杨松
 */
public interface IChatReadRecordService {


    /**
     * 获取会话组每个用户阅读情况
     * @param chatLogId
     * @return
     */
    List<ChatReadRecordRes> getChatReadRecordListByChatLogId(String chatLogId);

    /**
     * 更新为已读
     * @param updateChatRecordReq
     * @return
     */
    boolean updateReadRecord(UpdateChatRecordReq updateChatRecordReq);

    /**
     * 保存消息阅读记录（发送消息时初始化该记录）
     * @param chatReadRecordReq
     * @return
     */
    boolean saveChatReadRecord(ChatReadRecordReq chatReadRecordReq);
}
