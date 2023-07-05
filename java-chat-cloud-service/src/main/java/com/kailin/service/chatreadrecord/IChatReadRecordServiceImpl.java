package com.kailin.service.chatreadrecord;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.kailin.dao.chat.entity.ChatReadRecord;
import com.kailin.dao.chat.mapper.ChatReadRecordMapper;
import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.request.chatreadrecord.UpdateChatRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;
import com.kailin.response.chatuser.ChatUserRes;
import com.kailin.service.chatuser.IChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
    private final IChatUserService iChatUserService;


    @Override
    public List<ChatReadRecordRes> getChatReadRecordListByCondition(ChatReadRecordReq chatReadRecordReq) {
        ChatReadRecord chatReadRecord = chatReadRecordConvert.req2do(chatReadRecordReq);
        List<ChatReadRecord> chatReadRecordList = chatReadRecordMapper.getChatReadRecordListByCondition(chatReadRecord);
        return chatReadRecordConvert.do2res(chatReadRecordList);
    }

    @Override
    public boolean updateReadRecord(UpdateChatRecordReq updateChatRecordReq) {
        return chatReadRecordMapper.updateReadRecord(updateChatRecordReq.getChatId(), updateChatRecordReq.getUserIdList());
    }

    @Override
    public boolean saveChatReadRecord(ChatReadRecordReq chatReadRecordReq) {
        // 获取会话组所有用户信息
        List<ChatUserRes> userListByChatId = iChatUserService.getUserListByChatId(chatReadRecordReq.getChatId());

        if (!CollectionUtils.isEmpty(userListByChatId)) {
            List<ChatReadRecord> chatReadRecordList = Lists.newArrayList();
            for (ChatUserRes chatUserRes : userListByChatId) {
                ChatReadRecord chatReadRecord = new ChatReadRecord();
                // 排除发送人
                if(!chatUserRes.getUserId().equals(chatReadRecordReq.getUserId())){
                    chatReadRecord.setUserId(chatUserRes.getUserId());
                }
                chatReadRecord.setChatId(chatReadRecordReq.getChatId());
                chatReadRecord.setChatLogId(chatReadRecordReq.getChatLogId());
                chatReadRecord.setIsRead((byte) 0);
                chatReadRecord.setCreateTime(new Date());

                chatReadRecordList.add(chatReadRecord);
            }

            return this.saveBatch(chatReadRecordList);
        }
        return false;
    }
}
