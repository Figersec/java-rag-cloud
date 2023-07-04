package com.kailin.dao.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kailin.dao.chat.entity.ChatReadRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 杨松
 * @since 2023-07-04
 */
public interface ChatReadRecordMapper extends BaseMapper<ChatReadRecord> {

    /**
     * 获取会话组每个用户阅读情况
     * @param chatReadRecord
     * @return
     */
    List<ChatReadRecord> getUserReadRecordList(@Param("do") ChatReadRecord chatReadRecord);
}
