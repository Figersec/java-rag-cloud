package com.kailin.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kailinjt.middleware.kp.common.api.entity.CommonKRMessage;
import com.kailinjt.middleware.kp.common.api.entity.KRMessage;
import com.kailinjt.middleware.kp.common.api.entity.WrapperKRMessage;
import com.kailinjt.middleware.kp.common.api.jackson.ProdExcludePropertiesJacksonFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;

/**
 * <h2>响应信息实体V2.0_beta</h2>
 *
 * @author Daizc-kl
 */
@Log4j2
@Setter
@Accessors(chain = true)
@ApiModel(value = "KResponse", description = "响应信息实体V2.0_beta")
public class KResponse<DATA> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final String FORMAT_STR = "00000";

    // @JsonIgnore
    private KRMessage krMessage;

    @ApiModelProperty("返回数据")
    private DATA data;

    @ApiModelProperty(value = "自定义附加响应数据体")
    private Object customData;

    @Deprecated
    private Integer total;

    public static <D> KResponse<D> success() {
        return success(CommonKRMessage.SUCCESS);
    }

    public static <D> KResponse<D> success(String message) {
        return success(CommonKRMessage.SUCCESS, message);
    }

    public static <D> KResponse<D> success(KRMessage krMessage) {
        KResponse<D> response = new KResponse<>();
        krMessage = (krMessage != null) ? krMessage : CommonKRMessage.SUCCESS;
        response.setKrMessage(krMessage);
        return response;
    }

    public static <D> KResponse<D> success(KRMessage krMessage, String message) {
        KResponse<D> response = new KResponse<>();
        krMessage = (krMessage != null) ? krMessage : CommonKRMessage.SUCCESS;
        krMessage = (message == null) ? krMessage : new WrapperKRMessage(krMessage, message);
        response.krMessage = krMessage;
        return response;
    }

    public static <D> KResponse<D> data(D data) {
        KResponse<D> success = KResponse.<D>success();
        success.setData(data);
        return success;
    }

    public static <D> KResponse<D> failed() {
        return KResponse.failed(CommonKRMessage.FAILED);
    }

    public static <D> KResponse<D> failed(KRMessage krMessage) {
        return KResponse.failed(krMessage, null);
    }

    public static <D> KResponse<D> failed(KRMessage krMessage, String message) {
        KResponse<D> response = new KResponse<>();
        krMessage = (krMessage != null) ? krMessage : CommonKRMessage.FAILED;
        krMessage = (message == null) ? krMessage : new WrapperKRMessage(krMessage, message);
        response.krMessage = krMessage;
        return response;
    }

    public DATA getData() {
        return data;
    }

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ProdExcludePropertiesJacksonFilter.class)
    public Object getCustomData() {
        return customData;
    }

    @ApiModelProperty("信息[如果有，没有则不返回]")
    public String getMessage() {
        return krMessage.getMessage();
    }

    @ApiModelProperty(value = "请求状态码", hidden = true)
    public int getCode() {
        return krMessage.getCode();
    }

    @ApiModelProperty(value = "请求状态(已弃用)", hidden = true)
    @Deprecated
    public String getStatus() {
        return krMessage.getCode() == 0 ? "成功" : "失败";
    }

    @Deprecated
    public Integer getTotal() {
        return total;
    }

    @Deprecated
    public KResponse<DATA> setTotal(Integer total) {
        log.warn("{}方法已被弃用,请及时更新代码", "setCount");
        this.total = total;
        return this;
    }

    @ApiModelProperty(value = "消息说明,5位数字码(已弃用)", hidden = true)
    @Deprecated
    public String getStatusCode() {
        Integer code = krMessage.getCode();
        String codeStr = String.valueOf(code);
        if (code > 99999) {
            codeStr = String.valueOf(code);
        } else {
            codeStr = FORMAT_STR.substring(0, FORMAT_STR.length() - codeStr.length()) + codeStr;
        }
        return codeStr;
    }

    @Deprecated
    public KResponse<DATA> setCount(Integer count) {
        return setTotal(count);
    }

    @Deprecated
    public KResponse<DATA> setErrorStackTrace(StackTraceElement[] errorStackTrace) {
        log.warn("{}方法已被弃用,请及时更新代码", "setErrorStackTrace");
        return this;
    }

    public boolean isSuccess() {
        return CommonKRMessage.SUCCESS.getCode() == this.getCode();
    }

    @JsonIgnore
    public boolean isFailure() {
        return !isSuccess();
    }

}
