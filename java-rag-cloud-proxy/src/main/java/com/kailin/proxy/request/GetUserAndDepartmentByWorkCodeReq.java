package com.kailin.proxy.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by silent on 2021/9/16 14:37
 * @author siyue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserAndDepartmentByWorkCodeReq {

    @Schema(description = "员工工号")
    private String workCode;

    @Schema(description = "员工状态【默认查所有】")
    private List<String> status;

    public GetUserAndDepartmentByWorkCodeReq(String workCode){
        this.workCode = workCode;
    }

    public List<String> getStatus(){
        return this.status != null ? this.status : Arrays.asList("0","1","2","3","4","5","6","7");
    }
}
