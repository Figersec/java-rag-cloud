package com.kailin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kailin.dao.demo.entity.DmUser;

/**
 * @Author chengpuhui
 * @Date 2021/12/23
 */
public interface UserService extends IService<DmUser> {

    /**
     * 通过id获取用户名称
     * @param id
     * @return
     */
    String getUsernameById(Long id);
}
