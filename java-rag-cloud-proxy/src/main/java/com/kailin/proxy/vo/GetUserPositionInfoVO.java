package com.kailin.proxy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "品牌value")
    private String brandValue;

    @Schema(description = "业态value")
    private String businessValue;

    @Schema(description = "部门value")
    private String departmentId;

    @Schema(description = "角色id")
    private String positionId;

    @Schema(description = "角色名称")
    private String positionName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "是否选中")
    private Boolean isSelect = false;

    public GetUserPositionInfoVO(String positionId, String positionName, Boolean isSelect) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.isSelect = isSelect;
    }
}
