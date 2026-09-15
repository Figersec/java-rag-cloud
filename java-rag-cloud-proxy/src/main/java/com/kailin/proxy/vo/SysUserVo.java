package com.kailin.proxy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "公司id")
    private String companyId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "kboss部门id")
    private String kbDepId;

    @Schema(description = "kbossId")
    private String kbossId;

    @Schema(description = "部门id")
    private String departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门全称")
    private String departmentFullName;

    @Schema(description = "工号")
    private String workCode;
}
