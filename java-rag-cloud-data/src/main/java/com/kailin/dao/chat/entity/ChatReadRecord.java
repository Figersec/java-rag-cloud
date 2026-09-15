package com.kailin.dao.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 杨松
 * @since 2023-07-04
 */
@Data
@TableName("chat_read_record")
@Schema(name = "ChatReadRecord对象", description = "")
public class ChatReadRecord {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "会话组id")
    private String chatId;

    @Schema(description = "聊天记录id")
    private String chatLogId;

    @Schema(description = "是否已读 0否 1是")
    private Byte isRead;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

}
