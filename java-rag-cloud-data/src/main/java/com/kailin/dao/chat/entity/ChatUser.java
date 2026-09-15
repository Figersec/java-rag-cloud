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
@TableName("chat_user")
@Schema(name = "ChatUser对象", description = "")
public class ChatUser {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "聊天id")
    private String chatId;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "BossUser KlChat")
    private String userType;

}
