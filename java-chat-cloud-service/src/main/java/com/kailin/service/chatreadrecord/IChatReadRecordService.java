package com.kailin.service.chatreadrecord;

import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;

import java.util.List;

/**
 * @author 杨松
 */
public interface IChatReadRecordService {


    /**
     * 获取会话组每个用户阅读情况
     * @param chatReadRecordReq
     * @return
     */
    List<ChatReadRecordRes> getUserReadRecordList(ChatReadRecordReq chatReadRecordReq);
}
