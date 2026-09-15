package com.kailin.proxy.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchDepartmentSearchReq implements Serializable {
    @Schema(description = "部门主键id")
    private List<String> departmentIds;

    @Schema(description = "kboss部门id")
    private List<String> kbDepIds;

    @Schema(description = "企业微信部门id")
    private List<String> wxDepIds;
}
