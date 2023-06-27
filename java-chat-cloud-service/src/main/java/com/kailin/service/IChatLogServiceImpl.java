package com.kailin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.dao.chat.mapper.ChatLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** 实现类
 * @author 杨松
 */
@Service
@Slf4j
public class IChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements IChatLogService{

    @Override
    public List<ChatLog> getChatContentByCondition(ChatLog chatLog) {
        return null;
    }

    @Override
    public boolean insertChatLog(ChatLog chatLog) {
        return false;
    }

    @Override
    public boolean updateReadStatus(ChatLog chatLog) {
        return false;
    }

    @Override
    public boolean clearChat(ChatLog chatLog) {
        return false;
    }
}
