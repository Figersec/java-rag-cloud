package com.kailin.controller;

import com.kailin.dao.chat.entity.DmUser;
import com.kailin.common.ErrorKRMessage;
import com.kailin.proxy.CustomerServiceProxy;
import com.kailin.proxy.vo.BusinessInfosVo;
import com.kailin.service.UserService;
import com.kailin.request.UserIdReq;
import com.kailin.response.UserInfoRes;
import com.kailinjt.middleware.kp.common.api.entity.KpResponse;
import com.kailinjt.middleware.kp.common.api.exception.KBException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制器
 * @Author chengpuhui
 * @Date 2021/12/23
 */
@RestController
@Api(tags = "用户管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @PostMapping("/getUsernameById")
    @ApiOperation("根据用户id获取用户名")
    public KpResponse<String> getUsernameById(@RequestBody @Validated UserIdReq req) {
        String res = userService.getUsernameById(req.getUserId());
        return KpResponse.data(res);
    }

    @PostMapping("/getUserInfoById")
    @ApiOperation("根据用户id获取用户详情")
    public KpResponse<UserInfoRes> getUserInfoById(@RequestBody @Validated UserIdReq req) {
        DmUser dmUser = userService.getById(req.getUserId());
        if (dmUser == null) {
            throw new KBException(ErrorKRMessage.USER_NOT_EXIST);
        }
        UserInfoRes res = new UserInfoRes();
        BeanUtils.copyProperties(dmUser, res);
        return KpResponse.data(res);
    }

    @PostMapping("/exceptionTest")
    @ApiOperation("异常测试接口")
    public KpResponse<String> exceptionTest(@RequestBody @Validated UserIdReq req) {
        int i = 10/0;
        return KpResponse.data(String.valueOf(i));
    }

    @GetMapping("/getCustomerInfo/{id}")
    @ApiOperation("获取客户信息")
    public KpResponse<BusinessInfosVo> getCustomerInfo(@PathVariable String id) {
        BusinessInfosVo data = customerServiceProxy.queryCustomerInfoById(id);
        return KpResponse.data(data);
    }
}
