package com.kailin.proxy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "部门信息")
public class DepartmentInfoVO {
    @Schema(description = "部门id(非kboss、本地生成id，非kb同步信息关联用此字段)")
    private String id;

    @Schema(description = "部门id(kb同步信息，用此字段)")
    private String kbDepartmentId;
    /**
     * 分部id(来源kboss)
     */
    private String companyId;
    /**
     * 父部门id(对应department_id)
     */
    private String parentId;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门全名
     */
    private String fullName;
    /**
     * 英文名字
     */
    private String nameEn;
    /**
     * 排序
     */
    private String sort;
    /**
     * 品牌
     */
    private String brandValue;
    /**
     * 事业部type
     */
    private String businessValue;

    /**
     * 跟部门
     */
    private String root;

    /**
     * 微信主键id
     */
    private String wxDepId;

    /**
     * 系统代码,区别kboss的部门
     */
    private String sysCode;
}
