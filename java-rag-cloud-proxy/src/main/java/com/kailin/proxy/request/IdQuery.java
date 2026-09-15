package com.kailin.proxy.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("主键查询")
@NoArgsConstructor
@AllArgsConstructor
public class IdQuery {

    @ApiModelProperty("主键id")
    @NotBlank(message = "主键不能为空")
    String id;
}
