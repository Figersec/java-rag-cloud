package com.kailin.service.chat;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.dao.chat.mapper.ChatLogMapper;
import com.kailin.dao.chat.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 实现类
 *
 * @author 杨松
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IChatServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements IChatService {

    private final ChatMapper chatMapper;
}
