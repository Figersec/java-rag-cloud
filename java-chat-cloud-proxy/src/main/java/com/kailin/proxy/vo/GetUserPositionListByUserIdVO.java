package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description:
 * @date 2021/8/17 11:29
 */
@Data
public class GetUserPositionListByUserIdVO {

    @ApiModelProperty(value = "用户角色数据第一层")
    private List<UserPositionListVO1> userPositionListVO1List = new ArrayList<>();

}
