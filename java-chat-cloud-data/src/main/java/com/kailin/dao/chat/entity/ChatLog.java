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
 * @since 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat_log")
@ApiModel(value="ChatLog对象", description="")
public class ChatLog extends Model<ChatLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "发送用户id")
    private String fromUserId;

    @ApiModelProperty(value = "接收用户id")
    private String toUserId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发送类型: 0群聊 1单聊")
    private Byte sendType;

    @ApiModelProperty(value = "房间id（业务id）")
    private String businessId;

    @ApiModelProperty(value = "发送状态：0成功 1失败")
    private String sendStatus;

    @ApiModelProperty(value = "已读 ：0否1是")
    private Byte isRead;

    @ApiModelProperty(value = "消息是否被清空 ：0否1是")
    private Byte isClear;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;



    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
