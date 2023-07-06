package com.kailin.service.chat;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.Chat;
import com.kailin.dao.chat.mapper.ChatMapper;
import com.kailin.request.chat.ChatReq;
import com.kailin.request.chatuser.ChatUserReq;
import com.kailin.response.chat.ChatRes;
import com.kailin.response.chatuser.ChatUserRes;
import com.kailin.service.chatuser.IChatUserService;
import com.kailinjt.middleware.kp.common.api.entity.KRMessageCommon;
import com.kailinjt.middleware.kp.common.api.exception.KBException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 实现类
 *
 * @author 杨松
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements IChatService {

    private final ChatMapper chatMapper;
    private final IChatUserService iChatUserService;
    private final ChatConvert chatConvert;



    @Override
    public ChatRes openChat(ChatReq chatReq) {
        String chatId = chatReq.getChatId();
        // 没有则创建房间
        if(StringUtils.isBlank(chatId)){
            if(CollectionUtils.isEmpty(chatReq.getChatUserList())||StringUtils.isBlank(chatReq.getChatUserList().get(0).getUserId())){
                throw new KBException(KRMessageCommon.PARAM_ERROR_400, "创建聊天失败,缺少用户信息");
            }
            if(StringUtils.isBlank(chatReq.getChatName())){
                throw new KBException(KRMessageCommon.PARAM_ERROR_400, "创建聊天失败,缺少聊天名称");
            }
            return createChat(chatReq);
        }
        //有聊天id则返回房间信息及用户列表
        return getChatById(chatId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatRes createChat(ChatReq chatReq) {
        // 创建聊天
        String chatName = chatReq.getChatName();
        Chat chat = new Chat();
        chat.setChatName(chatName);
        chatMapper.insert(chat);
        // 创建聊天用户
        List<ChatUserReq> chatUserList = chatReq.getChatUserList();
        for (ChatUserReq chatUserReq : chatUserList) {
            chatUserReq.setChatId(chat.getId());
        }
        iChatUserService.saveChatUser(chatUserList);
        return chatConvert.do2res(chat);
    }


    @Override
    public ChatRes getChatById(String chatId) {
        // 获取房间信息
        ChatRes chatRes = chatConvert.do2res(chatMapper.selectById(chatId));
        // 获取房间聊天用户
        List<ChatUserRes> chatUserList = iChatUserService.getUserListByChatId(chatId);
        chatRes.setChatUserResList(chatUserList);
        return chatRes;
    }

    @Override
    public boolean updateChat(ChatReq chatReq) {
        Chat chat = chatMapper.selectById(chatReq.getChatId());
        if(null == chat){
            throw new KBException(KRMessageCommon.PARAM_ERROR_400, "房间不存在");
        }
        BeanUtils.copyProperties(chatReq,chat);
        chatMapper.updateById(chat);
        return true;
    }
}
