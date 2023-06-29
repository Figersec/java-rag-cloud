package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by silent on 2021/4/27 10:18
 */
@Data
public class SysUserVo implements Serializable {

    private static final long serialVersionUID = 8825328180537802449L;

    private String userId;
    private String userName;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "kboss部门id")
    private String kbDepId;

    @ApiModelProperty("kbossId")
    private String kbossId;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "部门全称")
    private String departmentFullName;

    @ApiModelProperty(value = "工号")
    private String workCode;
}
