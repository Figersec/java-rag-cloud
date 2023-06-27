package com.kailin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kailin.dao.chat.entity.DmUser;
import com.kailin.dao.chat.mapper.DmUserMapper;
import com.kailin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author chengpuhui
 * @Date 2021/12/23
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<DmUserMapper, DmUser> implements UserService {

    @Override
    public String getUsernameById(Long id) {
        DmUser user = baseMapper.selectById(id);
        return user != null ? user.getUsername() : null;
    }
}
