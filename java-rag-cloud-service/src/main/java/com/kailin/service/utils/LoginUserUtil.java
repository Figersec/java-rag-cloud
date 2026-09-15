package com.kailin.service.utils;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.kailin.common.ErrorKRMessage;
import com.kailin.proxy.OrgProxy;
import com.kailin.proxy.request.GetUserListByIdsReq;
import com.kailin.proxy.vo.GetUserListInfoVO;
import com.kailin.proxy.vo.SysUserVo;
import com.kailin.util.OptionalUtils;
import com.kailin.api.CommonKRMessage;
import com.kailin.api.KBException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;

//获取当前的登录人
@Component
public class LoginUserUtil {

    @Autowired
    private OrgProxy orgProxy;

    public Optional<SysUserVo> getLoginUserOptional() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader("userId");
        String userName = request.getHeader("userName");
        try {
            userName = URLDecoder.decode(userName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StrUtil.isEmpty(userId) || StrUtil.isEmpty(userName)) {
            throw new KBException(ErrorKRMessage.REQUEST_HEADER_ERROR);
        }
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUserId(userId);
        sysUserVo.setUserName(userName);
        return Optional.of(sysUserVo);
    }

    @Nullable
    public SysUserVo getLoginUser() {
        return getLoginUserOptional().orElse(new SysUserVo());
    }

    public SysUserVo getCurrentUserThrow() {
        return OptionalUtils.getOrElseThrow(getLoginUser(), ErrorKRMessage.STATE_TIMEOUT);
    }

    public SysUserVo getCurrentUserDetail() {
        SysUserVo currentUserThrow = getCurrentUserThrow();
        GetUserListByIdsReq getUserListByIdsReq = new GetUserListByIdsReq();
        getUserListByIdsReq.setIsDetails(true);
        getUserListByIdsReq.setIdList(Lists.newArrayList(currentUserThrow.getUserId()));
        Map<String, GetUserListInfoVO> map = orgProxy.userMapByIds(getUserListByIdsReq);
        if (map == null) {
            throw new KBException(CommonKRMessage.FAILED, "当前用户信息不存在");
        }
        GetUserListInfoVO getUserListInfoVO = map.get(currentUserThrow.getUserId());
        if (getUserListInfoVO == null) {
            throw new KBException(CommonKRMessage.FAILED, "当前用户信息不存在");
        }
        SysUserVo res = new SysUserVo();
        BeanUtils.copyProperties(getUserListInfoVO, res);
        res.setUserId(currentUserThrow.getUserId());
        res.setUserName(currentUserThrow.getUserName());
        return res;
    }

}
