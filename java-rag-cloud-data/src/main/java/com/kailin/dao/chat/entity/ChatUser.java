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

/**
 * <p>
 *
 * </p>
 *
 * @author 杨松
 * @since 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat_user")
@ApiModel(value="ChatUser对象", description="")
public class ChatUser extends Model<ChatUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "聊天id")
    private String chatId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "BossUser KlChat")
    private String userType;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
