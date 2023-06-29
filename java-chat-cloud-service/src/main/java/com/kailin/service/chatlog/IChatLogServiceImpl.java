package com.kailin.service.chatlog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.dao.chat.mapper.ChatLogMapper;
import com.kailin.enums.CommonEnum;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.response.chatlog.ChatLogRes;
import com.kailin.service.utils.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现类
 *
 * @author 杨松
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements IChatLogService {

    private final ChatLogMapper chatLogMapper;
    private final ChatLogConvert chatLogConvert;
    private final LoginUserUtil loginUserUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertChatLog(ChatLogReq chatLogReq) {
        ChatLog chatLog = chatLogConvert.req2do(chatLogReq);
        chatLog.setRecall(CommonEnum.NO.getValue());
        chatLog.setUpdateTime(new Date());
        chatLog.setCreateTime(new Date());
        chatLogMapper.insert(chatLog);
        return true;
    }


    @Override
    public List<ChatLogRes> getChatLogByChatId(String chatId) {
        // 根据chatId查询聊天记录
        LambdaQueryWrapper<ChatLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatLog::getChatId, chatId);
        queryWrapper.eq(ChatLog::getRecall, CommonEnum.NO.getValue());
        queryWrapper.orderByAsc(ChatLog::getCreateTime);
        List<ChatLog> chatLogs = chatLogMapper.selectList(queryWrapper);
        return chatLogConvert.do2res(chatLogs);
    }

    @Override
    public boolean updateRecallStatus(String chatLogId, Integer recallStatus) {
        LambdaUpdateWrapper<ChatLog> update = new LambdaUpdateWrapper<>();
        update.set(ChatLog::getRecall, recallStatus);
        update.eq(ChatLog::getId, chatLogId);
        this.update(update);
        return true;
    }
}
