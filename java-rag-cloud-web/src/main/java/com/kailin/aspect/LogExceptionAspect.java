package com.kailin.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kailin.api.CommonKRMessage;
import com.kailin.api.KBException;
import com.kailin.api.KRMessage;
import com.kailin.api.KpResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.util.List;

/**
 * 日志和异常处理切面
 *
 * @Author chengpuhui
 * @Date 2021/12/24
 */
@Aspect
@Component
@Slf4j
public class LogExceptionAspect {

    @Pointcut("execution(public * com.kailin..controller..*Controller.*(..))")
    public void logExceptionPointCut() {
    }

    @Around(value = "logExceptionPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 返回参数类型
        Class<?> returnType = ((MethodSignature) pjp.getSignature()).getReturnType();
        // 只拦截api方法，其他方法直接pass...
        if (KpResponse.class.isAssignableFrom(returnType)) {
            long time1 = System.currentTimeMillis();
            String className = pjp.getTarget().getClass().getSimpleName();
            String methodName = pjp.getSignature().getName();
            Object[] args = pjp.getArgs();

            // 打印入参
            String inputParamInfo = this.getInputParamInfo(className, methodName, args);
            log.info(inputParamInfo);


            // 调用方法，处理异常
            Object result;
            try {
                result = pjp.proceed();
            } catch (Exception e) {
                result = exceptionDeal(e, inputParamInfo, pjp);
            }

            // 打印出参
            long time2 = System.currentTimeMillis();
            loggerForOutPut(className, methodName, time2 - time1, result);
            return result;
        }
        return pjp.proceed();
    }

    private String getInputParamInfo(String className, String methodName, Object[] args) {
        String inputArgJson = "";
        List<Object> argList = Lists.newArrayList();
        for (Object arg : args) {
            if (arg instanceof ServletRequest || arg instanceof ServletResponse) {
                continue;
            }
            argList.add(arg);
        }
        try {
            inputArgJson = (argList.size() <= 0 ? "" : JSON.toJSONString(argList));
        } catch (Exception e) {
            inputArgJson = "!!json解析error!!";
            log.warn("入参json解析error", e);
        }
        // 打印类名，方法名，传入参数
        return className + "  " + methodName + "  入参: " + inputArgJson;
    }

    private void loggerForOutPut(String className, String methodName, long time, Object object) {
        String outputArgJson = "";
        try {
            outputArgJson = JSON.toJSONString(object);
        } catch (Exception e) {
            outputArgJson = "!!json解析error!!";
            log.warn("出参json解析error", e);
        }
        // 打印类名，方法名，方法调用时间，返回参数
        log.info(className + "  " + methodName + "  time=" + time + "  出参: " + outputArgJson);
    }

    private KpResponse exceptionDeal(Exception exception, String inputParamInfo, ProceedingJoinPoint pjp) {
        KBException retError;

        if (exception instanceof KBException) {
            retError = (KBException) exception;
        } else {
            retError = new KBException(CommonKRMessage.SYS_ERROR, exception.getMessage());
            if (inputParamInfo == null) {
                String className = pjp.getTarget().getClass().getSimpleName();
                String methodName = pjp.getSignature().getName();
                Object[] args = pjp.getArgs();
                inputParamInfo = this.getInputParamInfo(className, methodName, args);
            }
            // 打印入参和异常信息
            log.error(inputParamInfo, exception);
        }

        return getResultObj(retError.getKrMessage());
    }

    private KpResponse getResultObj(KRMessage krMessage) {
        return KpResponse.failed(krMessage);
    }
}
