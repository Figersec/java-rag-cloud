package com.kailin.dao.chat.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @since 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat_log")
@ApiModel(value = "ChatLog对象", description = "")
public class ChatLog extends Model<ChatLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "发送用户id")
    private String sendUserId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "元数据(给第三方用)")
    private String meta;

    @ApiModelProperty(value = "撤回：0否1是")
    private Integer recall;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private Date updateTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
