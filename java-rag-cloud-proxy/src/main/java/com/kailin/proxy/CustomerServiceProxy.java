package com.kailin.proxy;

import com.google.common.collect.Lists;
import com.kailin.proxy.client.CustomerClient;
import com.kailin.proxy.vo.BusinessInfosVo;
import com.kailin.proxy.vo.NgBusinessInfosVoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author chengpuhui
 * @Date 2021/12/15
 */
@Service
@Slf4j
public class CustomerServiceProxy {

    @Autowired
    private CustomerClient customerClient;

    /**
     * 根据id获取客户信息
     * @param id
     * @return
     */
    public BusinessInfosVo queryCustomerInfoById(String id) {
        NgBusinessInfosVoReq req = new NgBusinessInfosVoReq();
        req.setIds(Lists.newArrayList(id));
        List<BusinessInfosVo> list = customerClient.getBookByContractId(req);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
