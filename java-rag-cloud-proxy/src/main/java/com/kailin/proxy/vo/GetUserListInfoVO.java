package com.kailin.proxy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "boss用户id")
    private String bossUserId;

    @Schema(description = "kbossId")
    private String kbossId;

    @Schema(description = "用户名字")
    private String userName;

    @Schema(description = "身份证")
    private String idCard;

    @Schema(description = "员工工号")
    private String workCode;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "登陆手机号、同步的中台")
    private String loginPhone;

    @Schema(description = "微信userid")
    private String wxUserId;

    // endregion

    // region 以上是基本信息、以下是详细信息

    @Schema(description = "职位id")
    private String jobTitleId;

    @Schema(description = "职位名称")
    private String jobTitleName;

    @Schema(description = "公司id")
    private String companyId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "部门id")
    private String departmentId;

    @Schema(description = "kboss部门id")
    private String kbDepId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "部门全称")
    private String departmentFullName;

    @Schema(description = "业态")
    private String businessForm;

    //@Schema(description = "上级userId")
    //private String parentUserId;

    @Schema(description = "上级kbUserId")
    private String parentKbUserId;

    //@Schema(description = "下级用户ids")
    //private String childrenUserIds;
    //
    //@Schema(description = "下级用户idList，根据上面个字段来的")
    //private List<String> childrenuseridlist;

    @Schema(description = "司龄")
    private BigDecimal workingAge;

    @Schema(description = "入职时间")
    private String companyStartDate;

    /**
     * 电子邮箱(kboss)
     */
    @Schema(description = "电子邮箱")
    private String email;

    /**
     * 生日(kboss)
     */
    @Schema(description = "生日")
    private Date birthday;

    /**
     * 性别:0-啥也不是、1-男、2-女(kboss)
     */
    @Schema(description = "性别:0-啥也不是、1-男、2-女(kboss)")
    private Integer sex;

    // endregion

    // region 下级信息

    //@Schema(description = "下级列表")
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
//    @Schema(description = "工号")
//    private String workCode;
}
