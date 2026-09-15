package com.kailin.proxy.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchDepartmentSearchReq implements Serializable {
    @ApiModelProperty("部门主键id")
    private List<String> departmentIds;

    @ApiModelProperty("kboss部门id")
    private List<String> kbDepIds;

    @ApiModelProperty("企业微信部门id")
    private List<String> wxDepIds;
}
