package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author chengpuhui
 * @Date 2021/12/29
 */
@Data
public class BusinessInfosVo implements Serializable {
    private static final long serialVersionUID = 4221765438391709127L;

    @ApiModelProperty(value = "商机id")
    private String id;
    @ApiModelProperty(value = "商机号码")
    private String businessCode;
    @ApiModelProperty(value = "商机名称")
    private String businessName;
    @ApiModelProperty(value = "客户级别")
    private String customerLevel;
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "统一社会信用证代码")
    private String creditCode;
    @ApiModelProperty(value = "身份证号码")
    private String idCard;
    @ApiModelProperty(value = "商机类型(0-新签 1-续签)")
    private Integer businessType;
    @ApiModelProperty(value = "商机阶段")
    private String businessStage;
    @ApiModelProperty(value = "省级区域字典代码")
    private String provinceCode;
    @ApiModelProperty(value = "市级区域字典代码")
    private String cityCode;
    @ApiModelProperty(value = "联系地址")
    private String address;
    @ApiModelProperty(value = "行业字典id")
    private String industryId;
    @ApiModelProperty(value = "企业规模字典id")
    private String enterpriseScaleId;
    @ApiModelProperty(value = "需求")
    private String requirement;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "传真")
    private String fax;
    @ApiModelProperty(value = "微信")
    private String wechat;
    @ApiModelProperty(value = "QQ")
    private String qq;
    @ApiModelProperty(value = "客户id")
    private String customerId;
}
