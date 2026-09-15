package com.kailin.dao.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 杨松
 * @since 2023-06-28
 */
@Data
@TableName("chat")
@Schema(name = "Chat对象", description = "")
public class Chat {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String chatName;

    private String chatNotice;

}
