package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author chengpuhui
 * @Date 2021/12/29
 */
@Data
public class NgBusinessInfosVoReq implements Serializable {
    @ApiModelProperty(value = "商机id")
    @NotEmpty(message = "商机id 不能为空")
    private List<String> ids;
}
