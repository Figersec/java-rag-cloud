package com.kailin.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "KResponse", description = "响应信息实体V2.0_beta")
public class OldKResponse<DATA> implements Serializable {

    private static final long serialVersionUID = -1L;

    @Schema(description = "请求状态")
    private String status;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "信息[如果有，没有则不返回]")
    private String message;

    @Schema(description = "消息说明数字码")
    private String statusCode;

    @Schema(description = "返回数据")
    private DATA data;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "自定义附加响应数据体")
    private Object customData;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @Schema(description = "异常堆栈信息 仅在dev环境返回")
    private StackTraceElement[] errorStackTrace;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Schema(description = "总条数[如果有，没有则不返回]")
    private Integer total;

    public boolean isSuccess() {
        return "00000".equals(this.statusCode);
    }

    @JsonIgnore
    public boolean isFailure() {
        return !isSuccess();
    }

}
