package com.kailin.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <h2>响应信息实体V2.0_beta</h2>
 *
 * @author Daizc-kl
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "KResponse", description = "响应信息实体V2.0_beta")
public class OldKResponse<DATA> implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty("请求状态")
    private String status;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty("信息[如果有，没有则不返回]")
    private String message;

    @ApiModelProperty(value = "消息说明数字码")
    private String statusCode;

    @ApiModelProperty("返回数据")
    private DATA data;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "自定义附加响应数据体")
    private Object customData;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(value = "异常堆栈信息 仅在dev环境返回")
    private StackTraceElement[] errorStackTrace;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty("总条数[如果有，没有则不返回]")
    private Integer total;

    public boolean isSuccess() {
        return "00000".equals(this.statusCode);
    }

    @JsonIgnore
    public boolean isFailure() {
        return !isSuccess();
    }

}
