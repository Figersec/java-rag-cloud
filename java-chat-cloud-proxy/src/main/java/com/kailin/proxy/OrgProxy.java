package com.kailin.proxy;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kailin.common.ErrorKRMessage;
import com.kailin.common.OldKResponse;
import com.kailin.proxy.client.OrgClient;
import com.kailin.proxy.request.BatchDepartmentSearchReq;
import com.kailin.proxy.request.GetUserAndDepartmentByWorkCodeReq;
import com.kailin.proxy.request.GetUserListByIdsReq;
import com.kailin.proxy.request.GetUserPositionListByUserIdReq;
import com.kailin.proxy.vo.*;
import com.kailinjt.middleware.kp.common.api.exception.KBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangziqin
 * @description
 * @date 2022/8/9
 */
@Slf4j
@Component
public class OrgProxy {

    @Autowired
    private OrgClient orgClient;

    /**
     * 查询用户信息
     *
     * @param data
     * @return
     */
    public Map<String, GetUserListInfoVO> userMapByIds(GetUserListByIdsReq data) {
        OldKResponse<Map<String, GetUserListInfoVO>> response = orgClient.userMapByIds(data);
        if (response == null) {
            log.info("调用orgClient.userMapByIds失败，返回null");
            throw new KBException(ErrorKRMessage.FAILED, "查询用户信息失败");
        }
        log.info("调用orgClient.userMapByIds返回值：" + JSON.toJSONString(response));
        return response.getData();
    }

    /**
     * 部门信息获取
     *
     * @param data
     * @return
     */
    public List<DepartmentInfoVO> departmentListByIds(BatchDepartmentSearchReq data) {
        OldKResponse<List<DepartmentInfoVO>> response = orgClient.departmentListByIds(data);
        if (response == null) {
            log.info("调用orgClient.userMapByIds失败，返回null");
            throw new KBException(ErrorKRMessage.FAILED, "查询用户信息失败");
        }
        log.info("调用orgClient.userMapByIds返回值：" + JSON.toJSONString(response));
        return response.getData();
    }

    public GetUserListInfoVO getUserById(String id) {
        Map<String, GetUserListInfoVO> userMap = mapUsers(Stream.of(id).collect(Collectors.toList()));
        if (MapUtil.isEmpty(userMap)) {
            return new GetUserListInfoVO();
        }
        return userMap.get(id);
    }

    public GetUserListInfoVO getUserDetailById(String id) {
        Map<String, GetUserListInfoVO> userMap = mapUsersDetail(Stream.of(id).collect(Collectors.toList()));
        if (MapUtil.isEmpty(userMap)) {
            return new GetUserListInfoVO();
        }
        return userMap.get(id);
    }

    public Map<String, GetUserListInfoVO> mapUsers(List<String> ids) {
        List<String> idList = ids.stream().filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
        GetUserListByIdsReq param = new GetUserListByIdsReq(idList, false, false);
        return orgClient.userMapByIds(param).getData();
    }

    public Map<String, GetUserListInfoVO> mapUsersDetail(List<String> ids) {
        List<String> idList = ids.stream().filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
        GetUserListByIdsReq param = new GetUserListByIdsReq(idList, true);
        OldKResponse<Map<String, GetUserListInfoVO>> mapOldKResponse = null;
        try {
            mapOldKResponse = orgClient.userMapByIds(param);
            return mapOldKResponse.getData();
        } catch (Exception e) {
            log.info("调用auth发生错误", e);
        }
        return null;
    }

    /**
     * 查询所有员工（包括离职，但一个工号可能存在多条信息，auth容易报selectOne but result multi错误）
     *
     * @param workCode
     * @return
     */
    public UserDetailsByWorkCodeVo getUserAndDepartmentByWorkCode(String workCode) {
        return orgClient.getUserAndDepartmentByWorkCode(new GetUserAndDepartmentByWorkCodeReq(workCode)).getData();
    }

    /**
     * 只查询在职员工
     */
    public UserDetailsByWorkCodeVo getOnActiveUserByWorkCode(String workCode) {
        GetUserAndDepartmentByWorkCodeReq req = new GetUserAndDepartmentByWorkCodeReq(workCode);
        req.setStatus(Arrays.asList("0", "1", "2", "3"));
        return orgClient.getUserAndDepartmentByWorkCode(req).getData();
    }

    public List<UserPositionListVO1> getRoleListByUserId(String userId) {
        GetUserPositionListByUserIdReq req = new GetUserPositionListByUserIdReq();
        req.setUserId(userId);
        OldKResponse<GetUserPositionListByUserIdVO> userPositionListByUserId = orgClient.getUserPositionListByUserId(req);
        return userPositionListByUserId.getData().getUserPositionListVO1List();
    }
}
