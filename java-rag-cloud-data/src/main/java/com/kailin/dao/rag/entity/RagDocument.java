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
@TableName("rag_document")
public class RagDocument {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String kbId;

    private String fileName;

    private String fileType;

    private String filePath;

    private String status;

    private Integer chunkCount;

    private String errorMsg;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Boolean isDelete;
}
