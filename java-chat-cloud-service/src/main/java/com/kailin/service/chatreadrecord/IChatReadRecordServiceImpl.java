package com.kailin.service.chatreadrecord;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatReadRecord;
import com.kailin.dao.chat.mapper.ChatReadRecordMapper;
import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.request.chatreadrecord.UpdateChatRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现类
 *
 * @author 杨松
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IChatReadRecordServiceImpl extends ServiceImpl<ChatReadRecordMapper, ChatReadRecord> implements IChatReadRecordService {

    private final ChatReadRecordMapper chatReadRecordMapper;
    private final ChatReadRecordConvert chatReadRecordConvert;


    @Override
    public List<ChatReadRecordRes> getChatReadRecordListByChatId(String chatId) {
        List<ChatReadRecord> chatReadRecordList = chatReadRecordMapper.getChatReadRecordListByChatId(chatId);
        return chatReadRecordConvert.do2res(chatReadRecordList);
    }

    @Override
    public boolean updateReadRecord(UpdateChatRecordReq updateChatRecordReq) {
        return chatReadRecordMapper.updateReadRecord(updateChatRecordReq.getChatId(),updateChatRecordReq.getUserIdList());
    }
}
