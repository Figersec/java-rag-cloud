package com.kailin.service.websocket.factory;


import com.kailin.enums.OperationTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象工厂
 *
 * @author 杨松
 */
@Slf4j
@Component
public class MessageFactory {
    private static final Map<String, String> beanName = new ConcurrentHashMap<>();

    static {
        for (OperationTypeEnum recoverTypeEnum : OperationTypeEnum.values()) {
            beanName.put(recoverTypeEnum.getValue(), recoverTypeEnum.getValue());
        }
    }

    @Autowired
    private Map<String, AbstractRecoverTypeExecutor> executorMap;

    /**
     * 执行器
     *
     * @param operationTypeEnum
     * @return
     */
    public AbstractRecoverTypeExecutor getExecutor(OperationTypeEnum operationTypeEnum) {
        String beanName = MessageFactory.beanName.get(operationTypeEnum.getValue());
        AbstractRecoverTypeExecutor executor = executorMap.get(beanName);
        return executor;
    }
}
