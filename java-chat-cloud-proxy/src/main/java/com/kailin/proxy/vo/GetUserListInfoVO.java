package com.kailin.proxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description: 用户信息装载
 * @date 2021/7/29 15:10
 */
@Data
public class GetUserListInfoVO {

    // region 基本信息

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty("boss用户id")
    private String bossUserId;

    @ApiModelProperty("kbossId")
    private String kbossId;

    @ApiModelProperty(value = "用户名字")
    private String userName;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "员工工号")
    private String workCode;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "登陆手机号、同步的中台")
    private String loginPhone;

    @ApiModelProperty(value = "微信userid")
    private String wxUserId;

    // endregion

    // region 以上是基本信息、以下是详细信息

    @ApiModelProperty(value = "职位id")
    private String jobTitleId;

    @ApiModelProperty(value = "职位名称")
    private String jobTitleName;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "kboss部门id")
    private String kbDepId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "部门全称")
    private String departmentFullName;

    @ApiModelProperty(value = "业态")
    private String businessForm;

    //@ApiModelProperty(value = "上级userId")
    //private String parentUserId;

    @ApiModelProperty(value = "上级kbUserId")
    private String parentKbUserId;

    //@ApiModelProperty(value = "下级用户ids")
    //private String childrenUserIds;
    //
    //@ApiModelProperty(value = "下级用户idList，根据上面个字段来的")
    //private List<String> childrenuseridlist;

    @ApiModelProperty("司龄")
    private BigDecimal workingAge;

    @ApiModelProperty("入职时间")
    private String companyStartDate;

    /**
     * 电子邮箱(kboss)
     */
    @ApiModelProperty("电子邮箱")
    private String email;

    /**
     * 生日(kboss)
     */
    @ApiModelProperty("生日")
    private Date birthday;

    /**
     * 性别:0-啥也不是、1-男、2-女(kboss)
     */
    @ApiModelProperty("性别:0-啥也不是、1-男、2-女(kboss)")
    private Integer sex;

    // endregion

    // region 下级信息

    //@ApiModelProperty(value = "下级列表")
    //private List<GetUserListInfoVO> childrenList;
    //
    //// endregion
    //
    //public List<String> getChildrenUserIdList() {
    //    return StrUtil.isNotBlank(childrenUserIds) ? Arrays.stream(childrenUserIds.split(",")).collect(Collectors.toList()):new ArrayList<>();
    //}
//
//    /**
//     * 工号
//     */
//    @ApiModelProperty("工号")
//    private String workCode;
}
