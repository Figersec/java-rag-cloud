package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description: 用户角色列表装载第一层级
 * @date 2021/8/30 10:42
 */
@Data
public class UserPositionListVO1 {

    @ApiModelProperty(value = "品牌value")
    private String brandValue;

    @ApiModelProperty(value = "品牌name")
    private String brandName;

    @ApiModelProperty(value = "角色列表")
    private List<GetUserPositionInfoVO> positionInfoVOList;

}
