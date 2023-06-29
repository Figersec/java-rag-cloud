package com.kailin.proxy.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description:
 * @date 2021/8/17 11:27
 */
@Data
public class GetUserPositionListByUserIdReq {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "品牌value")
    private String brandValue;

    @ApiModelProperty(value = "业态value")
    private String businessValue;

    @ApiModelProperty(value = "部门value")
    private String departmentId;

    @ApiModelProperty(value = "搜素信息，对用角色全称、角色简称")
    private String condition;

}
