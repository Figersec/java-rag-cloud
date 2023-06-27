/*
 *
 *      Copyright (c) 2018-2025, edu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the glng.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: glng
 *
 */

package com.kailin.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

/**
 * @author glng
 * @date 2018/6/27
 * Spring 工具类
 */
@Slf4j
@Service(value = "SpringContextHolder2")
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {


    private static ApplicationContext applicationContext = null;
    private static Boolean isDebug = null;
    private static Integer serverPort = -1;
    @Value("${spring.profiles.active: prop}")
    private String profiles;

    public static Boolean isDebug() {
        return null != isDebug && isDebug;
    }

    public static Integer getServerPort() {
        return serverPort;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;

        SpringContextHolder.isDebug = Objects.equals(applicationContext.getEnvironment().getProperty("debug"), "true");
        String serverPortStr = applicationContext.getEnvironment().getProperty("server.port");
        Optional.ofNullable(serverPortStr).ifPresent(s -> SpringContextHolder.serverPort = Integer.valueOf(s));
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("清理SpringContextHolder");
        }
        SpringContextHolder.applicationContext = null;
        SpringContextHolder.isDebug = null;
        SpringContextHolder.serverPort = null;
    }

    /**
     * 发布事件
     *
     * @param event
     */
    public static void publishEvent(ApplicationEvent event) {
        if (applicationContext == null) {
            return;
        }
        applicationContext.publishEvent(event);
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    @Override
    public void destroy() {
        SpringContextHolder.clearHolder();
    }


    public static String thisRequestPath() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
    }
}
