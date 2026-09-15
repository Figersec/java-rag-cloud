package com.kailin.proxy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created by silent on 2021/9/16 14:38
 */
@Data
public class UserDetailsByWorkCodeVo {

    @Schema(description = "员工编号")
    private String workCode;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户微信id")
    private String userWxId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "部门id")
    private String departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "组织架构全名")
    private String fullName;

    @Schema(description = "分公司ID")
    private String companyId;

    @Schema(description = "分公司名")
    private String companyName;

    @Schema(description = "业态value")
    private String businessValue;

    @Schema(description = "kb部门id")
    private String kbDepartmentId;

}
