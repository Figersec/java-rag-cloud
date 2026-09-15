package com.kailin.dao.util;

import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * 反射工具类
 *
 * @author 杨松
 */
public class ReflectUtil {

    /**
     * 分离最后一个代理的目标对象
     *
     * @param invocation
     * @return
     */
    public static MetaObject getRealTarget(Invocation invocation) {
        MetaObject metaStatementHandler = SystemMetaObject.forObject(invocation.getTarget());

        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }

        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }

        return metaStatementHandler;
    }

}
