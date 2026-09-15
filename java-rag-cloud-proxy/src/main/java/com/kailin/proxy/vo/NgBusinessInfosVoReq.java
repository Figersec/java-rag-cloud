package com.kailin.proxy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @Author chengpuhui
 * @Date 2021/12/29
 */
@Data
public class NgBusinessInfosVoReq implements Serializable {
    @Schema(description = "商机id")
    @NotEmpty(message = "商机id 不能为空")
    private List<String> ids;
}
