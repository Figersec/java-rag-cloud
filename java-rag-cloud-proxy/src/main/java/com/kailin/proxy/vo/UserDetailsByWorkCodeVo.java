package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by silent on 2021/9/16 14:38
 */
@Data
public class UserDetailsByWorkCodeVo {

    @ApiModelProperty(value = "员工编号")
    private String workCode;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户微信id")
    private String userWxId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "组织架构全名")
    private String fullName;

    @ApiModelProperty(value = "分公司ID")
    private String companyId;

    @ApiModelProperty(value = "分公司名")
    private String companyName;

    @ApiModelProperty(value = "业态value")
    private String businessValue;

    @ApiModelProperty(value = "kb部门id")
    private String kbDepartmentId;

}
