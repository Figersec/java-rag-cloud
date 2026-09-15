package com.kailin.dao.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
@TableName("chat_read_record")
@ApiModel(value="ChatReadRecord对象", description="")
public class ChatReadRecord extends Model<ChatReadRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "会话组id")
    private String chatId;

    @ApiModelProperty(value = "聊天记录id")
    private String chatLogId;

    @ApiModelProperty(value = "是否已读 0否 1是")
    private Byte isRead;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
