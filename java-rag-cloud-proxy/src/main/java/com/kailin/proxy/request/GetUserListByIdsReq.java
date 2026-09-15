package com.kailin.proxy.request;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangzhongqin
 * @version 1.0
 * @Description:
 * @date 2021/7/29 15:03
 */
@Data
@ApiModel(value = "根据用户id列表获取用户信息")
public class GetUserListByIdsReq {

    @ApiModelProperty(value = "用户id列表")
    private List<String> idList = new ArrayList<>();

    @ApiModelProperty(value = "wxUserIds")
    private List<String> wxUserIds = new ArrayList<>();

    @ApiModelProperty(value = "返回数据是否为userIdkey，false，则以企业微信idkey")
    private Boolean isUserIdMapKey = true;

    @ApiModelProperty(value = "是否详情、true则获取详细信息")
    private Boolean isDetails = false;

    @ApiModelProperty(value = "是否获取下级数据、详情为true && 当前参数为 true 才会获取数据")
    private Boolean isHasChildren = true;

    @ApiModelProperty(value = "是否只要在岗数据,默认在岗，反之选所有")
    private Boolean isOnlyOnDuty = true;

    public GetUserListByIdsReq() {
    }

    public GetUserListByIdsReq(List<String> idList) {
        this.idList = idList;
    }

    public GetUserListByIdsReq(List<String> idList, Boolean isDetails) {
        this.idList = idList;
        this.isDetails = isDetails;
    }

    public GetUserListByIdsReq(List<String> idList, Boolean isDetails, Boolean isHasChildren) {
        this.idList = idList;
        this.isDetails = isDetails;
        this.isHasChildren = isHasChildren;
    }

    public static GetUserListByIdsReq buildByWxUserIdList(List<String> wxUserIdList,Boolean isUserIdMapKey){
        GetUserListByIdsReq getUserListByIdsReq = new GetUserListByIdsReq();
        getUserListByIdsReq.setWxUserIds(wxUserIdList);
        getUserListByIdsReq.setIsUserIdMapKey(isUserIdMapKey);
        return getUserListByIdsReq;
    }

    public static GetUserListByIdsReq buildByWxUserId(String wxUserId,Boolean isUserIdMapKey){
        GetUserListByIdsReq getUserListByIdsReq = new GetUserListByIdsReq();
        List<String> wxUserIdList = new ArrayList<>();
        wxUserIdList.add(wxUserId);
        getUserListByIdsReq.setWxUserIds(wxUserIdList);
        getUserListByIdsReq.setIsUserIdMapKey(isUserIdMapKey);
        return getUserListByIdsReq;
    }

    public static GetUserListByIdsReq buildByWxUserId(String wxUserId,Boolean isUserIdMapKey,Boolean isDetails){
        GetUserListByIdsReq getUserListByIdsReq = new GetUserListByIdsReq();
        List<String> wxUserIdList = new ArrayList<>();
        wxUserIdList.add(wxUserId);
        getUserListByIdsReq.setWxUserIds(wxUserIdList);
        getUserListByIdsReq.setIsUserIdMapKey(isUserIdMapKey);
        getUserListByIdsReq.setIsDetails(isDetails);
        return getUserListByIdsReq;
    }

    public List<String> getIdList() {
        if(CollUtil.isEmpty(idList)){
            return idList;
        }else {
            return idList.stream().filter(e-> StrUtil.isNotBlank(e)).collect(Collectors.toList());
        }
    }

    public List<String> getWxUserIds() {
        if(CollUtil.isEmpty(wxUserIds)){
            return wxUserIds;
        }else {
            return wxUserIds.stream().filter(e-> StrUtil.isNotBlank(e)).collect(Collectors.toList());
        }
    }
}
