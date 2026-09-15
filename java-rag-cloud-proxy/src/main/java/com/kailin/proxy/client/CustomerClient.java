package com.kailin.proxy.client;

import com.kailin.common.ServiceNameConstant;
import com.kailin.proxy.vo.BusinessInfosVo;
import com.kailin.proxy.vo.NgBusinessInfosVoReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author chengpuhui
 * @Date 2021/12/29
 */
@FeignClient(contextId = ServiceNameConstant.NG_KBOSS_CUSTOMER, value = ServiceNameConstant.NG_KBOSS_CUSTOMER)
public interface CustomerClient {

    /**
     * 根据合同ID查账本
     * @param request
     * @return
     */
    @PostMapping("/" + ServiceNameConstant.NG_KBOSS_CUSTOMER + "/business/getBusinessByIds")
    List<BusinessInfosVo> getBookByContractId(@RequestBody NgBusinessInfosVoReq request);
}
