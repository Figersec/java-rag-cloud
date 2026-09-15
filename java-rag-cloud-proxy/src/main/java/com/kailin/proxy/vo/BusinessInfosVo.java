package com.kailin.proxy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author chengpuhui
 * @Date 2021/12/29
 */
@Data
public class BusinessInfosVo implements Serializable {
    private static final long serialVersionUID = 4221765438391709127L;

    @Schema(description = "商机id")
    private String id;
    @Schema(description = "商机号码")
    private String businessCode;
    @Schema(description = "商机名称")
    private String businessName;
    @Schema(description = "客户级别")
    private String customerLevel;
    @Schema(description = "客户类型")
    private String customerType;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "公司名称")
    private String companyName;
    @Schema(description = "统一社会信用证代码")
    private String creditCode;
    @Schema(description = "身份证号码")
    private String idCard;
    @Schema(description = "商机类型(0-新签 1-续签)")
    private Integer businessType;
    @Schema(description = "商机阶段")
    private String businessStage;
    @Schema(description = "省级区域字典代码")
    private String provinceCode;
    @Schema(description = "市级区域字典代码")
    private String cityCode;
    @Schema(description = "联系地址")
    private String address;
    @Schema(description = "行业字典id")
    private String industryId;
    @Schema(description = "企业规模字典id")
    private String enterpriseScaleId;
    @Schema(description = "需求")
    private String requirement;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "传真")
    private String fax;
    @Schema(description = "微信")
    private String wechat;
    @Schema(description = "QQ")
    private String qq;
    @Schema(description = "客户id")
    private String customerId;
}
