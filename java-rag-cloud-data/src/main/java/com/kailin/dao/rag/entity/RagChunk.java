package com.kailin.dao.rag.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("rag_chunk")
public class RagChunk {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String kbId;

    private String docId;

    private Integer chunkIndex;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableLogic
    private Boolean isDelete;
}
