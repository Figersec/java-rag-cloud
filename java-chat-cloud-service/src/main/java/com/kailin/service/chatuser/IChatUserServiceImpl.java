package com.kailin.service.chatuser;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.ChatLog;
import com.kailin.dao.chat.mapper.ChatLogMapper;
import com.kailin.dao.chat.mapper.ChatMapper;
import com.kailin.dao.chat.mapper.ChatUserMapper;
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
public class IChatUserServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements IChatUserService {

    private final ChatUserMapper chatUserMapper;
}
