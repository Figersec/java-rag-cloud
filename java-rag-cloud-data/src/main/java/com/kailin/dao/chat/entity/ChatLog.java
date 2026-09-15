package com.kailin.dao.chat.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 杨松
 * @since 2023-06-27
 */
@Data
@TableName("chat_log")
@Schema(name = "ChatLog对象", description = "")
public class ChatLog {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "聊天id")
    private String chatId;

    @Schema(description = "发送用户id")
    private String sendUserId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "元数据(给第三方用)")
    private String meta;

    @Schema(description = "撤回：0否1是")
    private Integer recall;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private Date updateTime;

}
