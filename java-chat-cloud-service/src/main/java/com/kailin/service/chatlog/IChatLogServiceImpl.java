package com.kailin.service.chatlog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.dao.chat.mapper.ChatLogMapper;
import com.kailin.enums.CommonEnum;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.response.chatlog.ChatLogRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 实现类
 * @author 杨松
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements IChatLogService{

    private final ChatLogMapper chatLogMapper;
    private final ChatLogConvert chatLogConvert;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertChatLog(ChatLogReq chatLogReq) {
        ChatLog chatLog = chatLogConvert.req2do(chatLogReq);
        chatLog.setRecall(CommonEnum.NO.getValue());
        chatLogMapper.insert(chatLog);
        return true;
    }


    @Override
    public List<ChatLogRes> getChatLogByChatId(String chatId) {
        LambdaQueryWrapper<ChatLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatLog::getChatId, chatId);
        queryWrapper.eq(ChatLog::getRecall, CommonEnum.NO.getValue());
        List<ChatLog> chatLogs = chatLogMapper.selectList(queryWrapper);
        return chatLogConvert.do2res(chatLogs);
    }


}
