package com.kailin.proxy.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description:
 * @date 2021/8/17 11:27
 */
@Data
public class GetUserPositionListByUserIdReq {

    @Schema(description = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @Schema(description = "品牌value")
    private String brandValue;

    @Schema(description = "业态value")
    private String businessValue;

    @Schema(description = "部门value")
    private String departmentId;

    @Schema(description = "搜素信息，对用角色全称、角色简称")
    private String condition;

}
