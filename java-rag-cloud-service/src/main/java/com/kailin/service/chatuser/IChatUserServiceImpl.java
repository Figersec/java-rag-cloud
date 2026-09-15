package com.kailin.service.chatuser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatUser;
import com.kailin.dao.chat.mapper.ChatUserMapper;
import com.kailin.request.chatuser.ChatUserReq;
import com.kailin.response.chatuser.ChatUserRes;
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
public class IChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUser> implements IChatUserService {

    private final ChatUserMapper chatUserMapper;
    private final ChatUserConvert chatUserConvert;


    @Override
    public boolean saveChatUser(List<ChatUserReq> chatUserList) {
        return this.saveBatch(chatUserConvert.req2do(chatUserList));
    }


    @Override
    public List<ChatUserRes> getUserListByChatId(String chatId) {
        LambdaQueryWrapper<ChatUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatUser::getChatId,chatId);
        List<ChatUser> chatUsers = chatUserMapper.selectList(queryWrapper);
        return chatUserConvert.do2res(chatUsers);
    }
}
