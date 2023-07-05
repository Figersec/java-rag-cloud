package com.kailin.service.chatlog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.dao.chat.mapper.ChatLogMapper;
import com.kailin.enums.CommonEnum;
import com.kailin.proxy.OrgProxy;
import com.kailin.proxy.request.GetUserListByIdsReq;
import com.kailin.proxy.vo.GetUserListInfoVO;
import com.kailin.request.chatlog.ChatLogReq;
import com.kailin.response.chatlog.ChatLogRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private final OrgProxy orgProxy;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatLog insertChatLog(ChatLogReq chatLogReq) {
        ChatLog chatLog = chatLogConvert.req2do(chatLogReq);
        chatLog.setRecall(CommonEnum.NO.getValue());
        chatLog.setUpdateTime(new Date());
        chatLog.setCreateTime(new Date());
        chatLogMapper.insert(chatLog);
        return chatLog;
    }


    @Override
    public List<ChatLogRes> getChatLogByChatId(String chatId) {
        // 根据chatId查询聊天记录
        LambdaQueryWrapper<ChatLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatLog::getChatId, chatId);
        queryWrapper.eq(ChatLog::getRecall, CommonEnum.NO.getValue());
        queryWrapper.orderByAsc(ChatLog::getCreateTime);
        List<ChatLog> chatLogs = chatLogMapper.selectList(queryWrapper);
        List<ChatLogRes> chatLogResList = chatLogConvert.do2res(chatLogs);
        // 填充用户名，// 正确来讲，这里应该还要加个用户来源
        if (!CollectionUtils.isEmpty(chatLogResList)) {
            List<String> userIds = chatLogResList.stream().map(ChatLogRes::getSendUserId).distinct().collect(Collectors.toList());
            GetUserListByIdsReq req = new GetUserListByIdsReq();
            req.setIdList(userIds);
            Map<String, GetUserListInfoVO> userMap = orgProxy.userMapByIds(req);
            for (ChatLogRes chatLogRes : chatLogResList) {
                if(StringUtils.isNotBlank(chatLogRes.getSendUserId())){
                    chatLogRes.setUsername(Optional.ofNullable(userMap.get(chatLogRes.getSendUserId())).orElse(new GetUserListInfoVO()).getUserName());
                }
            }
        }else{
            chatLogResList = new ArrayList<>();
        }
        return chatLogResList;
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
