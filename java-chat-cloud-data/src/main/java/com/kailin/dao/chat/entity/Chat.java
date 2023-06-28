package com.kailin.dao.chat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("chat")
@ApiModel(value="Chat对象", description="")
public class Chat extends Model<Chat> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String chatName;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
