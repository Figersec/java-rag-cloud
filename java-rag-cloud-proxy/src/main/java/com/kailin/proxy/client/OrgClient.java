package com.kailin.proxy.client;

import com.kailin.common.OldKResponse;
import com.kailin.constant.ServiceNameConstants;
import com.kailin.proxy.request.*;
import com.kailin.proxy.vo.DepartmentInfoVO;
import com.kailin.proxy.vo.GetUserListInfoVO;
import com.kailin.proxy.vo.GetUserPositionListByUserIdVO;
import com.kailin.proxy.vo.UserDetailsByWorkCodeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description:
 * @date 2021/7/28 17:55
 */
@FeignClient(contextId = "orgClient", value = ServiceNameConstants.NG_KBOSS_AUTH)
public interface OrgClient {

    /**
     * 部门至顶层上级部门id
     *
     * @param idQuery
     * @return
     */
    @PostMapping(value = "/" + ServiceNameConstants.NG_KBOSS_AUTH + "/org/getDepartmentTreeIdPath")
    OldKResponse<List<String>> getDepartmentTreeIdPath(@RequestBody @Validated IdQuery idQuery);

    /**
     * 根据用户idList获取用户基本信息
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/" + ServiceNameConstants.NG_KBOSS_AUTH + "/org/userMapByIds", headers = {"resourceHeader=TRUE"})
    OldKResponse<Map<String, GetUserListInfoVO>> userMapByIds(@RequestBody @Validated GetUserListByIdsReq req);

    @PostMapping(value = "/" + ServiceNameConstants.NG_KBOSS_AUTH + "/org/departmentListByIds")
    OldKResponse<List<DepartmentInfoVO>> departmentListByIds(BatchDepartmentSearchReq req);

    /**
     * 通过员工工号来查询
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/" + ServiceNameConstants.NG_KBOSS_AUTH + "/org/getUserAndDepartmentByWorkCode")
    OldKResponse<UserDetailsByWorkCodeVo> getUserAndDepartmentByWorkCode(@RequestBody @Validated GetUserAndDepartmentByWorkCodeReq req);


    /**
     * 根据用户id查询角色
     *
     * @param req
     * @return
     */
    @PostMapping("/" + ServiceNameConstants.NG_KBOSS_AUTH + "/org/getUserPositionListByUserId")
    OldKResponse<GetUserPositionListByUserIdVO> getUserPositionListByUserId(@RequestBody @Validated GetUserPositionListByUserIdReq req);

//    /**
//     * 根据用户id查询角色
//     *
//     * @param getRoleRequest
//     * @return
//     */
//    @PostMapping({"/" + ServiceNameConstants.JAVA_RBAC_CLOUD +"/position/getRoleByUserIdAndRoleId"})
//    KResponse<Boolean> getRoleByUserIdAndRoleId(@RequestBody @Validated GetRoleRequest getRoleRequest);
}
