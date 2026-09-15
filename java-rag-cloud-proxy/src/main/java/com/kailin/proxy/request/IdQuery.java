package com.kailin.proxy.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "主键查询")
@NoArgsConstructor
@AllArgsConstructor
public class IdQuery {

    @Schema(description = "主键id")
    @NotBlank(message = "主键不能为空")
    String id;
}
