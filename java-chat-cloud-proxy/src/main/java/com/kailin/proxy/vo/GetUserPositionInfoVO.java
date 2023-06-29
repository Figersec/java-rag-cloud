package com.kailin.proxy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description:
 * @date 2021/8/17 11:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserPositionInfoVO {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "品牌value")
    private String brandValue;

    @ApiModelProperty(value = "业态value")
    private String businessValue;

    @ApiModelProperty(value = "部门value")
    private String departmentId;

    @ApiModelProperty(value = "角色id")
    private String positionId;

    @ApiModelProperty(value = "角色名称")
    private String positionName;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "是否选中")
    private Boolean isSelect = false;

    public GetUserPositionInfoVO(String positionId, String positionName, Boolean isSelect) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.isSelect = isSelect;
    }
}
